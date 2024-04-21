package com.data.center.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortThroughputVo {

    //港口
    private String port;

    //吞吐量
    private int throughput;

    //占比
    private BigDecimal percentage;
}