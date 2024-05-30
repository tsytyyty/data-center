package com.data.center.mapper;

import com.data.center.pojo.Do.PortThroughput;
import com.data.center.pojo.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Qualifier("PrimarySqlSessionTemplate")
public interface VisualMapper {

    @Select("select DESCRIPTION,URL from VISUAL;")
    List<VisualVo> selectVisualVo();

//    // 港货物吞吐量
//    List<PortGoodsThroughputVo> selectPortGoodsThroughput(@Param("port") String port);
//
//    // xx港吞吐量同比环比
//    List<PortYoyQoqVo> selectPortYoyQoq(int year);
//
//    // 港总吞吐量
//    List<PortThroughputVo> selectPortThroughput();
//
//    // xx港货物平均周转时间
//    List<PortGoodsFlowTimeVo> selectPortGoodsFlowTime(String port);
//
//    // xx港xx货物未来六月预测
//    List<PredictionVo> selectPrediction(@Param("port") String port);

}
