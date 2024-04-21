package com.data.center.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortGoodsFlowTimeVo {

    //货物
    private String goods;

    //平均流转时间
    private BigDecimal flowTime;
}
