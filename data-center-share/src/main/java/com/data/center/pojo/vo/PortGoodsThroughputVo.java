package com.data.center.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortGoodsThroughputVo {

    //港口
    private String port;

    //货物
    private String goods;

    //吞吐量
    private int throughput;

    //占比
    private BigDecimal percentage;

}
