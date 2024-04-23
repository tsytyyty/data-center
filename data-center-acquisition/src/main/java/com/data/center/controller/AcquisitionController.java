package com.data.center.controller;

import com.data.center.pojo.result.Result;
import com.data.center.service.DataAcquisitionService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
//@Tag(name = "数据采集")
public class AcquisitionController {

    @Autowired
    private DataAcquisitionService dataAcquisitionService;

    /**
     * 成功：200   失败：500
     * @return
     *         result.put("customerInformation", customerInformationList.size ());     //客户信息数据量（int）
     *         result.put("logisticsInformation", logisticsInformationList.size());    //提单信息数据量（int）
     *         result.put("loadingTable", loadingTableList.size());                    //装货表数据量（int）
     *         result.put("unloadingTable", unloadingTableList.size());                //卸货表数据量（int）
     *         result.put("errorList", errorList);                                     //错误信息（List<String>）
     */
    @GetMapping("/data/acquisition")
//    @Operation(summary = "数据采集api")
    public Result dataAcquisition() throws ExecutionException, InterruptedException {
        Map<String, Object> map = dataAcquisitionService.dataAcquisition();
        return new Result(0, "数据采集成功", map);
    }

}
