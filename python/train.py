import copy
import math
import os.path

import matplotlib.pyplot as plt
import numpy as np
import torch
import time
from torch import nn

# 定义损失函数
criterion = nn.MSELoss()
# 如果需要使用 L1 损失函数，可以切换注释
# criterion = nn.L1Loss()

# 模型保存路径
model_save_path = r'.\model\model.pt'
# 最佳损失保存路径
best_loss_save_path = r'.\model\best_loss'

# 设置设备
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


# 生成一个批次数据
def make_batch(source, batch_start_idx, batch_size):
    # 计算实际批次大小
    real_size = int(min(len(source[0])-1-batch_start_idx, batch_size))
    # 获取输入批次和标签批次
    input_batch = source[0][batch_start_idx:batch_start_idx+real_size, :]
    label_batch = source[1][batch_start_idx:batch_start_idx+real_size]
    return input_batch.to(device), label_batch.to(device)


# 训练函数
def train(model, train_data, batch_size, criterion, optimizer, scheduler):
    # 开始训练时的时间
    st_time = time.time()
    # 设置模型为训练模式
    model.train()
    total_loss = 0.0

    for batch_idx, idx in enumerate(range(0, len(train_data)-1, batch_size)):
        # 记录当前批次开始的时间
        batch_st_time = time.time()

        # 获取当前批次的数据
        batch, labels = make_batch(train_data, idx, batch_size)
        # 梯度清零
        optimizer.zero_grad()
        # 前向传播
        preds = model(batch)

        # 计算损失
        loss = criterion(preds, labels).cpu() * len(preds)
        # 反向传播
        loss.backward()
        # 梯度裁剪，防止梯度爆炸
        nn.utils.clip_grad_norm_(model.parameters(), 0.5)
        # 更新参数
        optimizer.step()

        # 记录总损失
        total_loss += loss.item()
        # 计算日志间隔
        log_interval = int(len(train_data) / batch_size / 5)
        # 打印日志
        if batch_idx != 0 and batch_idx % log_interval == 0:
            cur_loss = total_loss / log_interval
            duration = batch_st_time - time.time()

            print(f"\tbatch : {batch_idx:^8}\t{scheduler.get_last_lr()[0]:^10f}\t{duration:^6f}s\t{cur_loss:^6f}\t{math.exp(cur_loss):^6f}")

            total_loss = 0
        print(f"epoch total time : {time.time()-st_time:^8f}")


# 评估函数
def evaluate(model,val_data, batch_size, criterion, show_dialog=False):
    model.eval()
    total_loss = 0.0

    if show_dialog:
        result = torch.Tensor(0)
        truth = torch.Tensor(0)

    with torch.no_grad():
        for batch_idx, idx in enumerate(range(0, len(val_data)-1, batch_size)):
            batch, labels = make_batch(val_data, idx, batch_size)
            preds = model(batch)

            total_loss += criterion(preds, labels).cpu() * len(batch)
            if show_dialog:
                result = torch.cat((result, preds.view(-1).cpu()), 0)
                truth = torch.cat((truth, labels.view(-1).cpu()), 0)

    if show_dialog:
        plt.figure(figsize=(10, 6))
        plt.plot(result, color="C3", label='Prediction')  # Prediction val
        plt.plot(truth[:500], color="C0", label='True')  # True val
        plt.plot(result - truth, color="silver", label='Residual')  # Residual
        plt.grid(True, which='both')
        plt.axhline(y=0, color='k')
        plt.legend(loc='upper right')
        plt.show()
        plt.close()

    return total_loss / len(val_data)


# 训练和评估的函数
def train_eval(model, train_data, val_data, batch_size=12, epochs=1,test_step=12 , months_step=1, best_loss=None):
    # 定义优化器和学习率调度器
    optimizer = torch.optim.AdamW(model.parameters(), lr=1e-3, eps=1e-7, weight_decay=1e-5)
    scheduler = torch.optim.lr_scheduler.StepLR(optimizer, step_size=1, gamma=0.1)

    # 将数据移动到设备上
    train_data = [i.to(device) for i in train_data]
    val_data = [i.to(device) for i in val_data]

    # 如果没有指定最佳损失，设置为正无穷
    if best_loss is None:
        best_loss = float('inf')
        best_model = model

    # 记录开始时间
    st_time = time.time()

    # 开始迭代训练
    for epoch in range(1, epochs+1):
        print("="*30)
        print(f"Epoch : {epoch}")
        print("-"*30)

        # 训练模型
        train(model, train_data, batch_size, criterion, optimizer, scheduler)
        # 每隔一定周期进行验证，并显示对话框
        if epoch % test_step == 0:
            val_loss = evaluate(model, val_data, batch_size, criterion, show_dialog=True)
        else:
            val_loss = evaluate(model, val_data, batch_size, criterion, show_dialog=False)

        print("-"*30)
        # 打印验证损失
        print(f"Validation : loss {val_loss:^6f}") #PPL {math.exp(val_loss):^6f}
        print("=" * 30)

    # 如果最佳损失文件存在，读取最佳损失
    if os.path.exists(best_loss_save_path):
        with open(best_loss_save_path, 'r') as f:
            best_loss = float(f.read())
    else:
        with open(best_loss_save_path, 'w') as f:
            best_loss = float('inf')
            f.write('inf')

    # 如果当前验证损失低于最佳损失，则更新最佳损失并保存模型
    if val_loss < best_loss:
        best_loss = val_loss
        if os.path.exists(best_loss_save_path):
            with open(best_loss_save_path, 'w') as f:
                f.write(f"{best_loss}")
        best_model = copy.deepcopy(model)
        best_model.to('cpu')
        # 保存模型
        torch.save(best_model.state_dict(), model_save_path)
    else:
        print(f"当前val_loss : {val_loss} 大于目的指标 best_loss : {best_loss}")

    # 调度学习率
    scheduler.step()
    print("\nDone!")


# 模型推理函数
def predict(model, batch):
    model.eval()
    batch = batch.to(device)

    output = model(batch)
    output = output.to('cpu').detach().numpy()
    return output
