package com.data.center.data;

import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 清洗后数据
 */
public interface AfterCleanData {

    //客户信息
    Set<String> idCardSet = new HashSet<>();    //身份证号，去重自增
    List<CustomerInformation> clean_customer_information = new ArrayList<>();
//    List<CustomerInformation> dirty_customer_information = new ArrayList<>();

    //提单信息
    Map<String, String> logisticsInformationMap = new HashMap<>();  //提单号，去重
    List<LogisticsInformation> clean_logistics_information = new ArrayList<>();
//    List<LogisticsInformation> dirty_logistics_information = new ArrayList<>();

    //装货表
    Set<String> loadingTableSet = new HashSet<>();      //提单号，去重
    List<LoadingTable> clean_loading_table = new ArrayList<>();
//    List<LoadingTable> dirty_loading_table = new ArrayList<>();

    //卸货表
    Set<String> unloadingTableSet = new HashSet<>();    //提单号，去重
    List<UnloadingTable> clean_unloading_table = new ArrayList<>();
//    List<UnloadingTable> dirty_unloading_table = new ArrayList<>();

}
