package com.data.center.data;

import com.data.center.pojo.Do.*;

import java.util.ArrayList;
import java.util.List;

public interface AfterAnalysisData {

    List<TabulateData> totalData = new ArrayList<>();

    //港口吞吐量
    List<PortThroughput> portThroughputList = new ArrayList<>();

    //货物吞吐量
    List<GoodsThroughput> goodsThroughputList = new ArrayList<>();

    //货物-港口-吞吐量-占比
    List<PortGoodsThroughput> portGoodsThroughputList = new ArrayList<>();

    //港口-货物-入口量-占比
    List<PortGoodsInput> portGoodsInputList = new ArrayList<>();

    //港口-货物-出口量-占比
    List<PortGoodsOutput> portGoodsOutputList = new ArrayList<>();

    //港口-年份-月份-吞吐量-同比-环比
    List<PortYoyQoq> portYoyQoqList = new ArrayList<>();

    //港口-货物-流转时间
    List<PortGoodsFlowTime> portGoodsFlowTimeList = new ArrayList<>();



}
