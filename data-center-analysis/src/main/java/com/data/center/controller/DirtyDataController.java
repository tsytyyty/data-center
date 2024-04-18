package com.data.center.controller;

import com.data.center.contant.RedisConstant;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import com.data.center.pojo.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/error/data")
@Tag(name = "异常数据")
public class DirtyDataController implements RedisConstant {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/list/customerInformation")
    public Result customerError(@RequestParam int page, @RequestParam int number) {

        RMap<Object, Object> redisMap = redissonClient.getMap(DIRTY_CUSTOMER_INFORMATION);
        List<CustomerInformation> list = new ArrayList<>();
        redisMap.getAll(redisMap.keySet()).values().stream().forEach(customerInformation -> {
            list.add((CustomerInformation) customerInformation);
        });
        int beginNum = (page - 1) * number;
        int endNum = beginNum + number;
        if (beginNum >= list.size()){
            return Result.error("没有更多数据了！");
        }
        return Result.success("查询成功！", list.subList(beginNum, endNum));
    }


    @GetMapping("/list/logisticsError")
    public Result logisticsError(@RequestParam int page, @RequestParam int number) {

        RMap<Object, Object> redisMap = redissonClient.getMap(DIRTY_LOGISTICS_INFORMATION);
        List<LogisticsInformation> list = new ArrayList<>();
        redisMap.getAll(redisMap.keySet()).values().stream().forEach(logistics -> {
            list.add((LogisticsInformation) logistics);
        });
        int beginNum = (page - 1) * number;
        int endNum = beginNum + number;
        if (beginNum >= list.size()){
            return Result.error("没有更多数据了！");
        }
        return Result.success("查询成功！", list.subList(beginNum, endNum));
    }


    @GetMapping("/list/loadingError")
    public Result loadingError(@RequestParam int page, @RequestParam int number) {

        RMap<Object, Object> redisMap = redissonClient.getMap(DIRTY_LOADING_TABLE);
        List<LoadingTable> list = new ArrayList<>();
        redisMap.getAll(redisMap.keySet()).values().stream().forEach(loadingTable -> {
            list.add((LoadingTable) loadingTable);
        });
        int beginNum = (page - 1) * number;
        int endNum = beginNum + number;
        if (beginNum >= list.size()){
            return Result.error("没有更多数据了！");
        }
        return Result.success("查询成功！", list.subList(beginNum, endNum));
    }


    @GetMapping("/list/unloadingError")
    public Result unloadingError(@RequestParam int page, @RequestParam int number) {

        RMap<Object, Object> redisMap = redissonClient.getMap(DIRTY_UNLOADING_TABLE);
        List<UnloadingTable> list = new ArrayList<>();
        redisMap.getAll(redisMap.keySet()).values().stream().forEach(unloadingTable -> {
            list.add((UnloadingTable) unloadingTable);
        });
        int beginNum = (page - 1) * number;
        int endNum = beginNum + number;
        if (beginNum >= list.size()){
            return Result.error("没有更多数据了！");
        }
        return Result.success("查询成功！", list.subList(beginNum, endNum));
    }

}
