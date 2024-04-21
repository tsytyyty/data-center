package com.data.center.service;

import com.data.center.pojo.Do.PortGoodsThroughput;
import com.data.center.pojo.Do.PortYoyQoq;
import com.data.center.pojo.vo.PortGoodsThroughputVo;
import com.data.center.pojo.vo.PortYoyQoqVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface VisualService {

    Map<String, List<PortGoodsThroughputVo>> selectPortGoodsThroughput();

    Map<String, List<PortYoyQoqVo>> selectPortYoyQoq(int year);

    Map<String, Object> selectPortTotal(String port);


}
