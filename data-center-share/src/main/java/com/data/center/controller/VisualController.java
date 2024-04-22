package com.data.center.controller;

import com.data.center.pojo.Do.PortGoodsThroughput;
import com.data.center.pojo.result.Result;
import com.data.center.pojo.vo.PortGoodsThroughputVo;
import com.data.center.pojo.vo.PortYoyQoqVo;
import com.data.center.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visual/data")
public class VisualController {

    @Autowired
    private VisualService visualService;

    @GetMapping("/portGoodsThroughput")
    public Result portGoodsThroughput() {
        //查询
        Map<String, List<PortGoodsThroughputVo>> map = visualService.selectPortGoodsThroughput();
        return Result.success("查询成功！", map);
    }

    @GetMapping("/YoYQoQ")
    public Result YoYQoQ(@RequestParam int year) {
        //查询
        Map<String, List<PortYoyQoqVo>> map = visualService.selectPortYoyQoq(year);
        if (map == null){
            return Result.error("暂无该年份数据！");
        }
        return Result.success("查询成功！", map);
    }


    @GetMapping("/portTotal")
    public Result portTotal() {
        //查询

        return Result.success("查询成功！");
    }



}
