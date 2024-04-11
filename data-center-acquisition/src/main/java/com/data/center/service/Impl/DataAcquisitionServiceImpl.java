package com.data.center.service.Impl;

import com.data.center.factory.DataSource;
import com.data.center.factory.DataSourceFactory;
import com.data.center.pojo.Do.*;
import com.data.center.service.DataAcquisitionService;
import com.data.center.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.*;

@Service
public class DataAcquisitionServiceImpl implements DataAcquisitionService {

    @Autowired
    private DataSourceService dataSourceService;

    public Map<String, Integer> dataAcquisition() throws InterruptedException, ExecutionException {

        Map<String, Integer> result = new HashMap<>();
        List<DataSourceDo> doList = dataSourceService.getAllDataSource();
        //建立线程池
        ExecutorService threadPool = new ThreadPoolExecutor(
                doList.size(),
                doList.size(),
                doList.size(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //创建Callable集合
        List<Callable<Map<String, List<Object>>>> callableList = new ArrayList<>();
        for (DataSourceDo dataSourceDo : doList) {
            DataSource dataSource = DataSourceFactory.build(dataSourceDo);
            callableList.add(dataSource.dataAcquisition());
        }
        //知行所有callable
        List<Future<Map<String, List<Object>>>> futures = threadPool.invokeAll(callableList);
        //创建数据集合
        List<CustomerInformation> customerInformationList = new ArrayList<>();
        List<LoadingTable> loadingTableList = new ArrayList<>();
        List<UnloadingTable> unloadingTableList = new ArrayList<>();
        List<LogisticsInformation> logisticsInformationList = new ArrayList<>();

        System.out.println(futures.size());
        //遍历所有数据
        for (Future<Map<String, List<Object>>> future : futures) {
            Map<String, List<Object>> dataMap = future.get();
            dataMap.forEach((name, list) -> {
                Object ob = list.get(0);
                if (ob instanceof CustomerInformation){
                    list.forEach(o -> customerInformationList.add((CustomerInformation) o));
                } else if (ob instanceof LogisticsInformation) {
                    list.forEach(o -> logisticsInformationList.add((LogisticsInformation) o));
                } else if (ob instanceof LoadingTable) {
                    list.forEach(o -> loadingTableList.add((LoadingTable) o));
                } else if (ob instanceof UnloadingTable) {
                    list.forEach(o -> unloadingTableList.add((UnloadingTable) o));
                }
            });
        }

        result.put("customerInformation", customerInformationList.size());
        result.put("logisticsInformation", logisticsInformationList.size());
        result.put("loadingTable", loadingTableList.size());
        result.put("unloadingTable", unloadingTableList.size());

        return result;
    }


}
