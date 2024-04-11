package com.data.center.controller;

import com.data.center.pojo.result.Result;
import com.data.center.service.DataAcquisitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@Tag(name = "数据采集")
public class AcquisitionController {

    @Autowired
    private DataAcquisitionService dataAcquisitionService;

    /**
     * 成功：200   失败：500
     * @return
     * 成功：返回四种数据数量
     * 失败：返回错误数据源信息
     */
    @GetMapping("/data/acquisition")
    @Operation(summary = "数据采集api")
    public Result dataAcquisition() throws ExecutionException, InterruptedException {
        Map<String, Integer> map = dataAcquisitionService.dataAcquisition();
        return new Result(200, "数据采集成功", map);
    }

}
