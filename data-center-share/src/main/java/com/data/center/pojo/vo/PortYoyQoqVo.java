package com.data.center.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortYoyQoqVo {

    //港口名称
    private String port;

    //月份
    private int month;

    //吞吐量
    private int throughput;

    //同比
    private BigDecimal yoy;

    //环比
    private BigDecimal qoq;
}
