package com.data.center.mapper;

import com.data.center.pojo.Do.PortThroughput;
import com.data.center.pojo.vo.PortGoodsFlowTimeVo;
import com.data.center.pojo.vo.PortGoodsThroughputVo;
import com.data.center.pojo.vo.PortThroughputVo;
import com.data.center.pojo.vo.PortYoyQoqVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper
@Qualifier("PrimarySqlSessionTemplate")
public interface VisualMapper {

    // 港货物吞吐量
    List<PortGoodsThroughputVo> selectPortGoodsThroughput(@Param("port") String port);

    // xx港吞吐量同比环比
    List<PortYoyQoqVo> selectPortYoyQoq(int year);

    // 港总吞吐量
    List<PortThroughputVo> selectPortThroughput();

    // xx港货物平均周转时间
    List<PortGoodsFlowTimeVo> selectPortGoodsFlowTime(String port);

}
