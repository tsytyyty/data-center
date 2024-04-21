package com.data.center.mapper;

import com.data.center.pojo.Do.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResultMapper {

    /**
     * TabulateData同步数据
     */
    int tabulateData(List<TabulateData> list);

    /**
     * 港口吞吐量
     */
    int portThroughput(List<PortThroughput> list);

    /**
     * 货物吞吐量
     */
    int goodsThroughput(List<GoodsThroughput> list);

    /**
     * 货物-港口-吞吐量-占比
     */
    int portGoodsThroughput(List<PortGoodsThroughput> list);

    /**
     * 港口-货物-入口量-占比
     */
    int portGoodsInput(List<PortGoodsInput> list);

    /**
     * 港口-货物-出口量-占比
     */
    int portGoodsOutput(List<PortGoodsOutput> list);

    /**
     * 同比-环比
     */
    int yoyQoqEveryYear(List<PortYoyQoq> list);

    /**
     * 港口-货物-流转时间
     */
    int portGoodsFlowTime(List<PortGoodsFlowTime> list);

    /**
     * 删除表
     */
    @Delete("delete from ${tableName};")
    int deleteTable(@Param("tableName")String tableName);


}
