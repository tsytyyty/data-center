package com.data.center.service.Impl;

import com.data.center.mapper.VisualMapper;
import com.data.center.pojo.Do.PortGoodsThroughput;
import com.data.center.pojo.Do.PortYoyQoq;
import com.data.center.pojo.vo.*;
import com.data.center.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisualServiceImpl implements VisualService {

    @Autowired
    private VisualMapper visualMapper;

//    @Override
//    public Map<String, List<PortGoodsThroughputVo>> selectPortGoodsThroughput() {
//        Map<String, List<PortGoodsThroughputVo>> map = new HashMap<>();
//        List<PortGoodsThroughputVo> total = visualMapper.selectPortGoodsThroughput(null);
//
//        total.forEach(p -> {
//            if (map.containsKey(p.getPort())){
//                map.get(p.getPort()).add(p);
//            }else {
//                List<PortGoodsThroughputVo> list = new ArrayList<>();
//                list.add(p);
//                map.put(p.getPort(), list);
//            }
//        });
//        map.put("total", total);
//        return map;
//    }
//
//    @Override
//    public Map<String, List<PortYoyQoqVo>> selectPortYoyQoq(int year) {
//        Map<String, List<PortYoyQoqVo>> map = new HashMap<>();
//        List<PortYoyQoqVo> total = visualMapper.selectPortYoyQoq(year);
//        if (total.size() > 0){
//            total.forEach(p -> {
//                if (map.containsKey(p.getPort())){
//                    map.get(p.getPort()).add(p);
//                }else {
//                    List<PortYoyQoqVo> list = new ArrayList<>();
//                    list.add(p);
//                    map.put(p.getPort(), list);
//                }
//            });
//        }
//        return map;
//    }
//
//    @Override
//    public Map<String, Object> selectPortTotal(String port) {
//        Map<String, Object> map = new HashMap<>();
//        // 获取港吞吐量
//        List<PortThroughputVo> portsThroughputVos = visualMapper.selectPortThroughput();
//        // 货物吞吐量
//        List<PortGoodsThroughputVo> portGoodsThroughputVos = visualMapper.selectPortGoodsThroughput(port);
//        // 货物平均周转时间
//        List<PortGoodsFlowTimeVo> portGoodsFlowTimeVos = visualMapper.selectPortGoodsFlowTime(port);
//        // 货物未来6月总吞吐预测
//        List<PredictionVo> predictionVos = visualMapper.selectPrediction(port);
//        predictionVos.forEach(p -> {
//            p.computeTotal();
//        });
//
//        map.put("portsThroughput", portsThroughputVos);
//        map.put("goodsFlowTime", portGoodsFlowTimeVos);
//        map.put("goodsThroughput", portGoodsThroughputVos);
//        map.put("prediction", predictionVos);
//        return null;
//    }

    @Override
    public List<VisualVo> getAllVisualUrl() {
        List<VisualVo> visualVos = visualMapper.selectVisualVo();
        return visualVos;
    }
}
