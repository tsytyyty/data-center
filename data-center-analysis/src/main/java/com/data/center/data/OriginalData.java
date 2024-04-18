package com.data.center.data;

import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 原始数据
 */
public interface OriginalData {

    List<List<CustomerInformation>> data_customer_information = Collections.synchronizedList(new ArrayList<>());
    List<List<LogisticsInformation>> data_logistics_information = Collections.synchronizedList(new ArrayList<>());
    List<List<LoadingTable>> data_loading_table = Collections.synchronizedList(new ArrayList<>());
    List<List<UnloadingTable>> data_unloading_table = Collections.synchronizedList(new ArrayList<>());

}
