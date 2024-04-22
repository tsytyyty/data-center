package com.data.center.controller;

import com.data.center.pojo.result.Result;
import com.data.center.service.DataAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    /**
     * 数据分析
     */
    @GetMapping("/data/analysis")
    public Result dataAnalysis() {
        dataAnalysisService.dataAnalysis();
        return Result.success("数据分析成功！",null);
    }

}
