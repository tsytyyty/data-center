package com.data.center.controller;

import com.data.center.pojo.SelectSqlDto;
import com.data.center.pojo.result.Result;
import com.data.center.service.OpenSqlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "sql工作台")
public class OpenSqlController {

    @Autowired
    private OpenSqlService openSqlService;


    /**
     * sql工作台（jdbc）
     * 查询失败返回sql错误信息
     */
    @PostMapping("/selectTable")
    @Operation(summary = "sql查询")
    public Result selectTable(@RequestBody SelectSqlDto dto){
        if (!(dto.getSql().contains("SELECT") || dto.getSql().contains("select"))){
            return new Result(500, "查询失败！", "权限不足！");
        }
        try {
            List<Map<String, Object>> list = openSqlService.selectData(dto.getUsername(), dto.getSql());
            //将查询的结果存储到Map中
            return new Result(200, "查询成功！", list);
        }catch (Exception e){
            log.error(e.getMessage());
            return new Result(500, "查询失败！", e.getMessage());
        }
    }


    /**
     * 持久化到数据库
     */
    @GetMapping("/saveResult")
    public Result saveResult(@RequestParam String username){
        //建立数据库表
        int code = openSqlService.saveData(username);
        return new Result(code, code == 200 ? "保存成功！" : "保存失败，数据为空", null);
    }


    /**
     * 导出到excel
     */
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam String username, HttpServletResponse response){
        openSqlService.exportData(response, username);
    }





}