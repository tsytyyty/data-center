package com.data.center.controller;

import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.pojo.dto.DataSourceDto;
import com.data.center.pojo.result.Result;
import com.data.center.service.DataSourceService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import com.data.center.utils.DtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
//@Tag(name = "数据源管理")
@RequestMapping("/dataSource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 新增数据源
     * 成功：200   文件不存在：404   测试失败：500   空值：999
     */
    @PostMapping("/add")
//    @Operation(summary = "添加数据源")
    public Result addDataSource(@RequestBody DataSourceDto dataSourceDto) throws Exception {
        DataSourceDo dataSourceDo = DtoUtil.DataSourceDtoToDo(dataSourceDto);
        Map<String, Object> map = null;
        if (dataSourceDo == null){
            return new Result(999,"参数不能为空！",null);
        }
        map = dataSourceService.addDataSource(dataSourceDo);
        int code = (int) map.get("code");
        if (code == 200){
            return new Result(0,"添加成功！", map.get("dataSourceDo"));
        } else if (code == 404) {
            return new Result(code,"文件路径或数据表不存在！", map.get("errorList"));
        } else if (code == 500) {
            return new Result(code,"连接测试未通过！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else {
            return new Result(code,"未知错误！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        }
    }

    /**
     * 测试数据源，添加之前
     * 成功：200   文件不存在：404    测试失败：500    空值：999
     */
    @PostMapping("/test/beforeAdd")
//    @Operation(summary = "测试数据源（添加数据源之前）")
    public Result testDataSourceBeforeAdd(@RequestBody DataSourceDto dataSourceDto) throws Exception {
        DataSourceDo dataSourceDo = DtoUtil.DataSourceDtoToDo(dataSourceDto);
        Map<String, Object> map = null;
        if (dataSourceDo == null){
            return new Result(999,"参数不能为空！",null);
        }
        map = dataSourceService.addDataSource(dataSourceDo);
        int code = (int) map.get("code");
        if (code == 200){
            return new Result(0,"添加成功！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else if (code == 404) {
            return new Result(code,"文件路径或数据表不存在！", map.get("errorList"));
        } else if (code == 500) {
            return new Result(code,"连接测试未通过！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else {
            return new Result(code,"未知错误！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        }
    }

    /**
     * 测试已有数据源
     * 成功：200   测试失败：500    空值：999
     */
    @PostMapping("/test")
//    @Operation(summary = "测试数据源（修改数据源之前）")
    public Result testDataSource(@RequestBody DataSourceDto dataSourceDto) throws Exception {
        DataSourceDo dataSourceDo = DtoUtil.DataSourceDtoToDo(dataSourceDto);
        if (dataSourceDo == null){
            return new Result(999,"参数不能为空！",null);
        }
        int code = dataSourceService.testDataSource(dataSourceDo);
        if (code == 200){
            return new Result(0,"测试通过！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else if (code == 500) {
            return new Result(code,"连接测试未通过！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else {
            return new Result(code,"未知错误！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        }
    }

    /**
     * 测试全部已有数据源
     */
    @GetMapping("/test/all")
//    @Operation(summary = "测试全部数据源")
    public Result testAllDataSource() throws Exception {
        List<DataSourceDo> dataSourceDos = dataSourceService.getAllDataSource();
        for (DataSourceDo dataSourceDo : dataSourceDos) {
            int code = dataSourceService.testDataSource(dataSourceDo);
            if (code != 200){
                return new Result(code,"连接测试未通过！", null);
            }
        }
        return new Result(0,"连接测试通过！", null);
    }

    /**
     * 修改数据源
     * 成功：200   未找到数据源：403   文件不存在：404   测试未通过：500     空值：999
     */
    @PostMapping("/update")
//    @Operation(summary = "修改数据源")
    public Result updateDataSource(@RequestBody DataSourceDto dataSourceDto) throws Exception {
        DataSourceDo dataSourceDo = DtoUtil.DataSourceDtoToDo(dataSourceDto);
        Map<String, Object> map = null;
        if (dataSourceDo == null){
            return new Result(999,"参数不能为空！",null);
        }
        map = dataSourceService.updateDataSource(dataSourceDo);
        int code = (int) map.get("code");
        if (code == 200){
            return new Result(0,"修改成功！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else if (code == 403) {
            return new Result(code,"未找到数据源id！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else if (code == 404) {
            return new Result(code,"文件路径或数据表不存在！", map.get("errorList"));
        } else if (code == 500) {
            return new Result(code,"连接测试未通过！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        } else {
            return new Result(code,"未知错误！", DtoUtil.DataSourceDoToDto(dataSourceDo));
        }
    }

    /**
     * 删除数据源
     * 成功：200   未找到：403   空值：999
     */
    @GetMapping("/delete")
//    @Operation(summary = "删除数据源")
    public Result deleteDataSource(@RequestParam String id) throws ClassNotFoundException {
        if (id == null){
            return new Result(999,"参数不能为空！",null);
        }
        int code = dataSourceService.deleteDataSource(id);
        return new Result(0,code == 200 ? "删除成功！" : "未找到数据源！",id);
    }

    @GetMapping("/list")
//    @Operation(summary = "查询所有数据源")
    public Result getAllDataSource(){
        List<DataSourceDo> list = dataSourceService.getAllDataSource();
        List<DataSourceDto> list1 = new ArrayList<>();
        for (DataSourceDo dataSourceDo : list) {
            DataSourceDto dataSourceDto = DtoUtil.DataSourceDoToDto(dataSourceDo);
            list1.add(dataSourceDto);
        }
        return new Result(0,"查询成功！", list1);
    }


}
