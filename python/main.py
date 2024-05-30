# import os.path
#
# import torch
# import torch.nn as nn
# import numpy as np
#
# from model import HarborPredict
# from process import preprocess, window_length, get_data_reflect_dict
# from train import train_eval
# from train import model_save_path, predict
#
# epoch = 60
# test_step = 60
# batch_size = 8
#
#
# device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
# train_size = 0.95 #决定多少数据用于训练
#
# np.random.seed(114514)
# # torch.manual_seed(114514)
#
# # 这是训练代码
# # if __name__ == '__main__':
# #     # 预处理数据
# #     inputs, labels = preprocess(batch_size)
# #     # 创建模型
# #     model = HarborPredict(input_channel=inputs.shape[-1], out_channel=window_length, dropout=0.4).to(device)
# #
# #     if os.path.exists(model_save_path):
# #         weight = torch.load(model_save_path)
# #         model.load_state_dict(weight)
# #
# #     # 分割数据
# #     idx = np.arange(start=0, stop=len(inputs), dtype='int32')
# #     np.random.shuffle(idx)
# #     train_arrange = int(len(idx)*train_size)
# #     train_idx = idx[:train_arrange]
# #     val_idx = idx[train_arrange:]
# #
# #     train_idx = sorted(train_idx)
# #     train_inputs = inputs.copy()
# #     train_labels = labels.copy()
# #     train_inputs[val_idx] = np.zeros_like(train_inputs[0])
# #     train_labels[val_idx] = 0.
# #     # 得到训练数据
# #     train_data = (torch.FloatTensor(train_inputs), torch.FloatTensor(train_labels))
# #
# #     val_inputs = inputs[val_idx]
# #     val_labels = labels[val_idx]
# #     dl = list()
# #     for i in range(len(val_inputs)):
# #         if not np.all(val_inputs[i] == 0):
# #             dl.append(i)
# #     val_inputs = val_inputs[dl]
# #     val_labels = val_labels[dl]
# #
# #     val_data = (torch.FloatTensor(val_inputs), torch.FloatTensor(val_labels))
# #
# #     # 训练数据
# #     train_eval(
# #         model,
# #         train_data=train_data,
# #         val_data=val_data,
# #         epochs=epoch,
# #         batch_size=batch_size,
# #         test_step=test_step
# #         )
# #
# #     batch = torch.FloatTensor(train_inputs[-batch_size:])
# #     reals = train_labels[-batch_size:]
# #     preds = predict(model, batch)
# #     reals = reals
# #
# #     for idx, i in enumerate(preds):
# #         print(f"预测值为 {i}, 真实值为 {reals[idx]}")
#
#
# def predict_future(raw_data, harbor:str, good:str):
#     '''
#     从港口先前若干月的数据预测未来若干月的数据
#     :param raw_data:一个长度不小于window_length的一维序列,表示先前港口货物输出数据
#     :param harbor:  港口名
#     :param good:    商品名
#     '''
#     raw_data = np.asarray(raw_data) / 100.0
#
#     reflect_dict = get_data_reflect_dict()
#     fix = harbor + '-' + good
#
#     if not fix in reflect_dict:  #如果没有对应的港口或商品数据，做默认预测
#         harbor_good_id = 0.
#     else:
#         harbor_good_id = reflect_dict[fix]
#
#     # 生成模型可处理的数据
#     input = np.asarray([raw_data, np.ones_like(raw_data)*harbor_good_id])
#     input = input.transpose(1, 0)[None, :]
#     input = torch.Tensor(input).to(device)
#
#     # 创建模型
#     model = HarborPredict(input_channel=input.shape[-1], out_channel=window_length, dropout=0.4).to(device)
#
#     if os.path.exists(model_save_path):
#         weight = torch.load(model_save_path)
#         model.load_state_dict(weight)
#
#     preds = predict(model, input)
#     return preds*100.0
#
# # 预测使用演示
# if __name__ == '__main__':
#     raw_data = [1200,1300,4324,342,43,543]
#     harbor = '南通港'
#     good = '三钙'
#
#     print(predict_future(raw_data, harbor, good))





import numpy as np
from scipy.optimize import linear_sum_assignment


# 假定的距离矩阵（您需要根据附件1和电子地图的实际数据来填充这个矩阵）
# 矩阵中的每个元素dist_matrix[i][j]表示从点i到点j的行驶时间
# 注意：这个矩阵应该是对称的，且对角线元素为0（表示不移动的时间）
dist_matrix = np.array([
    [0, 10, 15, 8, 7, 9, 11, 14],
    [10, 0, 5, 12, 4, 13, 6, 3],
    [15, 5, 0, 14, 10, 2, 1, 12],
    [8, 12, 14, 0, 6, 15, 9, 2],
    [7, 4, 10, 6, 0, 1, 8, 11],
    [9, 13, 2, 15, 1, 0, 13, 7],
    [11, 6, 1, 9, 8, 13, 0, 5],
    [14, 3, 12, 2, 11, 7, 5, 0]
])

# 使用匈牙利算法求解最小成本分配问题
row_ind, col_ind = linear_sum_assignment(dist_matrix)

# 生成最优路径
optimal_path = []
current_point = 0
for col in col_ind:
    optimal_path.append(current_point)
    current_point = col
optimal_path.append(current_point)  # 添加最后一个点，完成循环

# 输出最优路径
print("最优路径（采样点的顺序）:", optimal_path)

# 计算完成当天工作的最短时间
total_time = sum(dist_matrix[row_ind, col_ind])
print("完成当天工作的最短时间:", total_time)