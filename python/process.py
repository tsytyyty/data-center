import pandas as pd
import numpy as np
import torch
from torch.utils.data import Dataset, DataLoader
import os
from joblib import dump, load
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler

window_length = 6 #预测所需时间长度

data_path = r'.\data\tabulate_fix.csv'
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
# onehot_model_save_path = r'.\onehot.pt'


# if os.path.exists(onehot_model_save_path):
#     onehot_encoder = load(onehot_model_save_path)
# else:
#     onehot_encoder = OneHotEncoder(sparse_output=False)

# def to_onehot(raw_data):
#     '''在生成编码的同时对编码模型进行训练'''
#     # 去除大小写影响
#     raw_data = [[j.lower() for j in i] for i in raw_data]
#
#     # 进行将字符串转为[0,1]之间的编码
#     data_array = np.array(raw_data)
#     data_array = data_array.reshape(-1, 1)
#     data_array = onehot_encoder.fit_transform(data_array)
#
#     data_array = np.array(data_array, dtype='int32')
#     # 更新并保存模型
#     dump(onehot_encoder, onehot_model_save_path)
#     return data_array
#
#
# def get_onehot(s):
#     return onehot_encoder.transform(s)


class TimeSeriesDataset(Dataset):
    def __init__(self, data, sequence_length):
        self.data = data
        self.sequence_length = sequence_length

    def __len__(self):
        return len(self.data) - self.sequence_length

    def __getitem__(self, idx):
        X = self.data[idx:idx + self.sequence_length]
        y = self.data[idx + 1:idx + self.sequence_length + 1]
        return X, y

def timeseries_generator(dataset, batch_size, sequence_length, harbor=0):
    _dataset = TimeSeriesDataset(dataset, sequence_length)

    data_loader = DataLoader(
        _dataset,
        batch_size=batch_size,
        shuffle=True,
        num_workers=0,  # 如果在Windows上，设置为0
    )

    X_list = list()
    y_list = list()
    for X, y in data_loader:
        # X.append(torch.ones(X[0].size())*harbor)
        X = np.asarray(X)
        t_x = list()
        for i in X:
            t_x.append(np.asarray([i, np.ones_like(i)*harbor]).transpose(1, 0))
        X = np.asarray(t_x)
        y = np.asarray(y) #.transpose(1, 0)
        X_list.extend(X)
        y_list.extend(y)
    X = np.asarray(X_list)
    # X = np.concatenate((X, np.zeros((batch_size-X.shape[0], X.shape[1]))), axis=0)
    # X = X.transpose(1, 0)
    y = np.asarray(y_list)
    return X, y
def preprocess(batch_size):
    harbor_data = pd.read_csv(data_path, encoding='gbk')
    # row_shape : harbor, good, year, month, output

    data = dict()

    # 数据分解
    harbors = harbor_data['港口'].to_list()
    goods = harbor_data['货物'].to_list()
    years = harbor_data['年份'].to_list()
    months = harbor_data['月份'].to_list()
    outputs = harbor_data['吞吐量'].to_list()

    # todo:这里还缺个天气,之后可能要加
    # weathers = harbor_data['天气'].to_list()

    fix = [i+'-'+j for i, j in zip(harbors, goods)]
    fix_unique = list(set(fix))

    fix_dict = dict()
    for idx, i in enumerate(fix_unique):
        fix_dict[i] = idx

    fix = [fix_dict[i] for i in fix]

    for idx, i in enumerate(outputs):
        if fix[idx] not in data:
            data[fix[idx]] = list()
        data[fix[idx]].append([years[idx], months[idx], i])

    max_len = -1
    # 序列化
    out_inputs = list()
    out_labels = list()

    for fix_id in data.keys():
        temp = sorted(data[fix_id], key=lambda x: (x[0], x[1]))
        temp = np.asarray([i[2] for i in temp])
        X, y = timeseries_generator(temp, batch_size=batch_size, sequence_length=window_length, harbor=fix_id)

        # new_X = list()
        # for idx, i in enumerate(X):
        #     new_X.append([i, [fix_id] * window_length])
        #
        # if len(new_X) > max_len:
        #     max_len = len(new_X)

        out_inputs.extend(X)
        out_labels.extend(y)

    return np.asarray(out_inputs, dtype='float32')/100, np.asarray(out_labels, dtype='float32')/100

def get_data_reflect_dict():
    '''
    获取从 "港口名-货物名" 到 "编码" 的映射
    '''
    harbor_data = pd.read_csv(data_path, encoding='gbk')

    harbors = harbor_data['港口'].to_list()
    goods = harbor_data['货物'].to_list()

    fix = [i + '-' + j for i, j in zip(harbors, goods)]
    fix_unique = list(set(fix))

    fix_dict = dict()
    for idx, i in enumerate(fix_unique):
        fix_dict[i] = idx
    return fix_dict


# if '__main__' == __name__:
#     print(preprocess())