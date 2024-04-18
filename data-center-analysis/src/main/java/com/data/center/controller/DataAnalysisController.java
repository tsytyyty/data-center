package com.data.center.controller;

import com.data.center.pojo.result.Result;
import com.data.center.service.DataAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "数据分析")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    /**
     * 数据分析
     */
    @GetMapping("/data/analysis")
    @Operation(summary = "数据分析api")
    public Result dataAnalysis() {
        dataAnalysisService.dataAnalysis();
        return Result.success("数据分析成功！",null);
    }

}
