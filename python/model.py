import numpy as np
import torch
import torch.nn as nn
import math
import random

from process import window_length

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


# 生成遮罩
class MaskGenerator(nn.Module):
    def __init__(self, mask_size, mask_ratio, lm=-1):
        super().__init__()
        self.mask_size = mask_size
        self.mask_ratio = mask_ratio
        self.sort = True
        self.average_patch = lm
        # if self.distribution == "geom":
        #     assert lm != -1

    def uniform_rand(self):
        mask = list(range(int(self.mask_size)))
        random.shuffle(mask)
        mask_len = int(self.mask_size * self.mask_ratio)
        self.masked_tokens = mask[:mask_len]
        self.unmasked_tokens = mask[mask_len:]
        if self.sort:
            self.masked_tokens = sorted(self.masked_tokens)
            self.unmasked_tokens = sorted(self.unmasked_tokens)
        return self.unmasked_tokens, self.masked_tokens

    def forward(self):
        self.unmasked_tokens, self.masked_tokens = self.uniform_rand()
        return self.unmasked_tokens, self.masked_tokens


# 用于代替一维卷积
class DepthwiseSeparableConv1d(nn.Module):
    def __init__(self, in_channels, out_channels):
        super(DepthwiseSeparableConv1d, self).__init__()
        # 深度卷积和逐点卷积
        self.depthwise = nn.Conv1d(in_channels, in_channels, kernel_size=3, padding=1,
                                   stride=1, groups=in_channels)
        self.pointwise = nn.Conv1d(in_channels, out_channels, kernel_size=1, padding=0,
                                   stride=1)

    def forward(self, x):
        x = self.depthwise(x)
        x = self.pointwise(x)
        return x


# 位置编码，用于加强时间表示
class PositionalEncoding(nn.Module):
    def __init__(self, d_model, max_len=5000):
        super(PositionalEncoding, self).__init__()

        PE = torch.zeros(max_len, d_model)  # (5000, d_model)
        position = torch.arange(0, max_len, dtype=torch.float).unsqueeze(1)  # (5000, 1)
        div_term = torch.exp(torch.arange(0, d_model, 2).float() * (-math.log(10000.0) / d_model))
        PE[:, 0::2] = torch.sin(position * div_term)  # index = 2_i+1
        PE[:, 1::2] = torch.cos(position * div_term)  # index = 2_i
        PE = PE.unsqueeze(0).transpose(0, 1)  # (5000, 1, d_model)
        self.register_buffer('PE', PE)

    def forward(self, x):
        return x + self.PE[:x.size(0), :]  # Embedding vector + Positional Encoding


# MLP激活函数
class MLP(nn.Module):
    """
    feature_size : Embedding dimension
    dropout : Dropout ratio
    Gelu : Activation function
    """

    def __init__(self, feature_size=256, dropout=0.1):
        super().__init__()
        self.c_fc = nn.Linear(feature_size, 4 * feature_size, bias=True)
        self.c_proj = nn.Linear(4 * feature_size, 1, bias=True)
        self.dropout = nn.Dropout(dropout)

    def forward(self, x):
        x = self.c_fc(x)
        x = Gelu(x)
        x = self.c_proj(x)
        x = self.dropout(x)
        return x


class HarborPredict(nn.Module):

    def __init__(self, input_channel, out_channel, num_layers=3, dropout=0.1):
        super(HarborPredict, self).__init__()

        # 位置编码器，用于加强输入数据中的时间表示
        self.pos_encoder = PositionalEncoding(input_channel)

        # Dropout 层，用于在模型训练过程中进行随机失活，防止过拟合
        self.dropout = nn.Dropout(dropout)

        # ReLU 激活函数，引入非线性
        self.relu = nn.ReLU()

        # 深度可分离卷积层，用于特征提取
        self.spconv1 = DepthwiseSeparableConv1d(6, 16)
        self.spconv2 = DepthwiseSeparableConv1d(16, 32)

        # 池化层，用于降低特征维度
        self.maxpool1 = nn.MaxPool1d(1)
        self.maxpool2 = nn.MaxPool1d(1)
        self.maxpool3 = nn.MaxPool1d(1)
        self.maxpool4 = nn.MaxPool1d(1)

        # 一维卷积层，进一步处理特征
        self.conv1 = nn.Conv1d(in_channels=32, out_channels=64, kernel_size=2)
        self.conv2 = nn.Conv1d(in_channels=64, out_channels=32, kernel_size=1)
        self.conv3 = nn.Conv1d(in_channels=32, out_channels=32, kernel_size=1)
        self.conv4 = nn.Conv1d(in_channels=32, out_channels=16, kernel_size=1)

        # 线性层，用于特征的线性变换
        self.line2 = nn.Linear(in_features=16, out_features=32, bias=True)
        self.line3 = nn.Linear(in_features=32, out_features=16, bias=True)
        self.line4 = nn.Linear(in_features=16, out_features=8, bias=True)

        # 最后一个线性层，将模型输出映射到最终的预测值
        self.line_out = nn.Linear(in_features=8, out_features=out_channel, bias=True)

    # 初始化权重
    # self.init_weights()


def init_weights(self):
    init_range = 0.1
    self.pos_decoder.c_fc.bias.data.zero_()
    self.pos_decoder.c_fc.weight.data.uniform_(-init_range, init_range)
    self.pos_decoder.c_proj.bias.data.zero_()
    self.pos_decoder.c_proj.weight.data.uniform_(-init_range, init_range)


def _generate_look_ahead_mask(self, seq_len):
    mask = (torch.triu(torch.ones(seq_len, seq_len)) == 1).transpose(0, 1)
    mask = mask.float().masked_fill(mask == 0, float('-inf')).masked_fill(mask == 1, float(0.0))
    return mask


def forward(self, x):
    # if self.mask is None or self.mask.size(0) != len(x):
    #     device = x.device
    #     mask = self._generate_look_ahead_mask(len(x)).to(device)
    #     self.mask = mask
    # 位置编码
    x = self.pos_encoder(x)
    # # 编码器
    # x = self.transformer_encoder(x, self.mask)
    # # 解码
    # x = self.pos_decoder(x)

    # B, N, L = x.size()
    # x = x.view(B, N * L)
    #
    # x = self.line1(x)
    # x = self.relu(x)

    # B, N = x.size()
    # x = x.view(B, N, 1)

    x = self.spconv1(x)
    x = self.spconv2(x)
    x = self.relu(x)
    x = self.maxpool1(x)

    x = self.dropout(x)

    x = self.conv1(x)
    # x = self.relu(x)
    x = self.conv2(x)
    x = self.relu(x)
    x = self.maxpool2(x)

    x = self.dropout(x)

    x = self.conv3(x)
    x = self.relu(x)
    x = self.maxpool3(x)

    x = self.conv4(x)
    x = self.relu(x)
    x = self.maxpool4(x)

    x = self.dropout(x)

    B, N, L = x.size()
    x = x.view(B, N * L)

    x = self.line2(x)
    x = self.line3(x)
    x = self.relu(x)

    x = self.dropout(x)

    x = self.line4(x)
    x = self.relu(x)

    x = self.line_out(x)
    # x = x.view(-1)
    return x


def Gelu(x):
    return 0.5 * x * (1.0 + torch.tanh(math.sqrt(2.0 / math.pi) * (x + 0.044715 * torch.pow(x, 3.0))))


# def trunc_normal_(tensor, mean=0., std=1.):
#     nn.init.trunc_normal_(tensor, mean=mean, std=std, a=-std, b=std)

def unshuffle(shuffled_tokens):
    dic = dict()
    for k, v, in enumerate(shuffled_tokens):
        dic[v] = k

    unshuffle_index = list()
    for i in range(len(shuffled_tokens)):
        unshuffle_index.append(dic[i])
    return unshuffle_index
