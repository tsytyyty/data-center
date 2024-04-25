package com.data.center.pojo.vo;

import lombok.Data;

@Data
public class PredictionVo {

    //港口
    private String port;

    //货物
    private String goods;

    //月份1
    private int throughput1;

    //月份2
    private int throughput2;

    //月份3
    private int throughput3;

    //月份4
    private int throughput4;

    //月份5
    private int throughput5;

    //月份6
    private int throughput6;

    //总计
    private int total;

    public void computeTotal() {
        this.total = this.throughput1 + this.throughput2 + this.throughput3 + this.throughput4 + this.throughput5 + this.throughput6;
    }

}
