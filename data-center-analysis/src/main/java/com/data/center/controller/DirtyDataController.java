package com.data.center.controller;

import com.data.center.contant.RedisConstant;
import com.data.center.data.AfterCleanData;
import com.data.center.data.OriginalData;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import com.data.center.pojo.result.Result;
import com.data.center.utils.IdCardUtil;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/error/data")
public class DirtyDataController implements RedisConstant, AfterCleanData {

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 错误数据
     */
    @GetMapping("/errorTotal")
    public Result errorData(@RequestParam String type){
        int total;
        switch (type){
            case "customerError":
                total = redissonClient.getMap(DIRTY_CUSTOMER_INFORMATION).size();
                break;
            case "logisticsError":
                total = redissonClient.getMap(DIRTY_LOGISTICS_INFORMATION).size();
                break;
            case "unloadingError":
                total = redissonClient.getMap(DIRTY_LOADING_TABLE).size();
                break;
            case "loadingError":
                total = redissonClient.getMap(DIRTY_UNLOADING_TABLE).size();
                break;
            default:
                return null;
        }
        return new Result(0, "数据总数", total);
    }

    @GetMapping("/list/customerError")
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

    @PostMapping("/edit/customerError")
    public Result editCustomerError(@RequestBody CustomerInformation customerInformation){
        RMap<Object, Object> map = redissonClient.getMap(DIRTY_CUSTOMER_INFORMATION);
        CustomerInformation c = (CustomerInformation) map.get(customerInformation.getId());
        List<String> remark = new ArrayList<>();
        //判断身份证号是否合规
        if (!IdCardUtil.isIdCardNo(c.getIdCard())) {
            remark.add("身份证号不合规");
        }
        //异常值检测
        if (c.getPhoneNumber().length() != 11) {
            remark.add("电话号异常");
        }
        //去重
        if (idCardSet.contains(c.getIdCard())) {
            remark.add("数据重复");
        }
        if (remark.isEmpty()) {
            clean_customer_information.add(c);
            idCardSet.add(c.getIdCard());
            map.remove(c.getId());
            return Result.success("修改成功！");
        }else {
            c.setRemark(String.join(",", remark));
            map.put(c.getId(), c);
            return Result.error("修改失败！", c);
        }
    }

    @PostMapping("/edit/logisticsError")
    public Result editLogisticsError(@RequestBody LogisticsInformation logisticsInformation){
        RMap<Object, Object> map = redissonClient.getMap(DIRTY_LOGISTICS_INFORMATION);
        LogisticsInformation l = (LogisticsInformation) map.get(logisticsInformation.getId());
        List<String> remark = new ArrayList<>();
        //判断身份证号是否合规
        if (!IdCardUtil.isIdCardNo(l.getOwnerId())) {
            remark.add("身份证号不合规");
        }
        //判断物流信息中客户是否存在
        if (!idCardSet.contains(l.getOwnerId())){
            remark.add("客户不在客户信息中");
        }
        //异常值检测
        if (l.getWeight() <= 0) {
            remark.add("重量异常");
        }
        //去重
        if (logisticsInformationMap.containsKey(l.getLogisticsId())) {
            remark.add("数据重复");
        }
        if (remark.isEmpty()) {
            logisticsInformationMap.put(l.getLogisticsId(),l.getGoodsName() + "_" + l.getWeight());
            clean_logistics_information.add(l);
            return Result.success("修改成功！");
        }else {
            l.setRemark(String.join(",", remark));
            map.put(l.getId(), l);
            return Result.error("修改失败！", l);
        }
    }

    @PostMapping("/edit/loadingError")
    public Result editLoadingError(@RequestBody LoadingTable loadingTable){
        RMap<Object, Object> map = redissonClient.getMap(DIRTY_LOADING_TABLE);
        LoadingTable l = (LoadingTable) map.get(loadingTable.getId());
        List<String> remark = new ArrayList<>();
        //判断提单号是否在提单信息中
        if (!logisticsInformationMap.containsKey(l.getLogisticsId())) {
            remark.add("提单号不在提单信息中");
        }
        //去重
        if (loadingTableSet.contains(l.getLogisticsId())) {
            remark.add("数据重复");
        }

        if (remark.isEmpty()) {
            loadingTableSet.add(l.getLogisticsId());
            clean_loading_table.add(l);
            return Result.success("修改成功！");
        }else {
            l.setRemark(String.join(",", remark));
            map.put(l.getId(), l);
            return Result.error("修改失败！", l);
        }

    }

    @PostMapping("/edit/customerInformation")
    public Result editCustomerInformation(@RequestBody UnloadingTable unloadingTable){
        RMap<Object, Object> map = redissonClient.getMap(DIRTY_UNLOADING_TABLE);
        UnloadingTable l = (UnloadingTable) map.get(unloadingTable.getId());
        List<String> remark = new ArrayList<>();
        //判断提单号是否在提单信息中
        if (!logisticsInformationMap.containsKey(l.getLogisticsId())) {
            remark.add("提单号不在提单信息中");
        }
        //去重
        if (unloadingTableSet.contains(l.getLogisticsId())) {
            remark.add("数据重复");
        }

        if (remark.isEmpty()) {
            unloadingTableSet.add(l.getLogisticsId());
            clean_unloading_table.add(l);
            return Result.success("修改成功！");
        }else {
            l.setRemark(String.join(",", remark));
            map.put(l.getId(), l);
            return Result.error("修改失败！", l);
        }
    }

}
