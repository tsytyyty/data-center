package com.data.center.service.Impl;

import com.data.center.contant.RedisConstant;
import com.data.center.factory.DataSource;
import com.data.center.factory.DataSourceFactory;
import com.data.center.feignClient.DataAnalysisFeignClient;
import com.data.center.pojo.Do.*;
import com.data.center.service.DataAcquisitionService;
import com.data.center.service.DataSourceService;
import com.data.center.utils.AsyncSender;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class DataAcquisitionServiceImpl implements DataAcquisitionService, RedisConstant {

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private AsyncSender asyncSender;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DataAnalysisFeignClient feignClient;

    public Map<String, Object> dataAcquisition() throws InterruptedException, ExecutionException {

        Map<String, Object> result = new HashMap<>();
        List<Object> errorList = new ArrayList<>();
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
        //执行所有callable
        List<Future<Map<String, List<Object>>>> futures = threadPool.invokeAll(callableList);
        //创建数据集合
        List<CustomerInformation> customerInformationList = new ArrayList<>();
        List<LoadingTable> loadingTableList = new ArrayList<>();
        List<UnloadingTable> unloadingTableList = new ArrayList<>();
        List<LogisticsInformation> logisticsInformationList = new ArrayList<>();

        //遍历所有数据
        for (Future<Map<String, List<Object>>> future : futures) {
            Map<String, List<Object>> dataMap = future.get();
            dataMap.forEach((name, list) -> {
                Object ob = null;
                if (Objects.equals(name, "errorList")) {
                    errorList.addAll(list);
                }else {
                    ob = list.get(0);
                }

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
        threadPool.shutdown();

        //异步分批发送数据
        new Thread(() -> {
            //分布式锁
            boolean getLock = false;
            try {
                getLock = redissonClient.getLock(LOCK_TRANSMIT_CLEAN).tryLock(10, 10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (getLock){
                long begin = System.currentTimeMillis();
                feignClient.clearData();
                try {
                    int i;
                    List<Future<Boolean>> futureList = new ArrayList<>();
                    for (i = 0; i <= customerInformationList.size(); i += 5000){
                        if (i + 5000 >= customerInformationList.size()){
                            futureList.add(asyncSender.sendCus(customerInformationList.subList(i, customerInformationList.size())));
                            break;
                        }
                        futureList.add(asyncSender.sendCus(customerInformationList.subList(i, i + 5000)));
                    }
                    for (i = 0; i <= logisticsInformationList.size(); i += 5000){
                        if (i + 5000 >= logisticsInformationList.size()){
                            futureList.add(asyncSender.sendLog(logisticsInformationList.subList(i, logisticsInformationList.size())));
                            break;
                        }
                        futureList.add(asyncSender.sendLog(logisticsInformationList.subList(i, i + 5000)));
                    }
                    for (i = 0; i <= loadingTableList.size(); i += 5000){
                        if (i + 5000 >= loadingTableList.size()){
                            futureList.add(asyncSender.sendLoad(loadingTableList.subList(i, loadingTableList.size())));
                            break;
                        }
                        futureList.add(asyncSender.sendLoad(loadingTableList.subList(i, i + 5000)));
                    }
                    for (i = 0; i <= unloadingTableList.size(); i += 5000){
                        if (i + 5000 >= unloadingTableList.size()){
                            futureList.add(asyncSender.sendUnload(unloadingTableList.subList(i, unloadingTableList.size())));
                            break;
                        }
                        futureList.add(asyncSender.sendUnload(unloadingTableList.subList(i, i + 5000)));
                    }

                    //等待所有线程执行完毕
                    futureList.forEach(future -> {
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }finally {
                    System.out.println("=======================================================================" + (System.currentTimeMillis() - begin));
                    //解锁
                    redissonClient.getLock(LOCK_TRANSMIT_CLEAN).unlock();
                }
            }else {
                System.out.println("==========================================================================获取锁失败");
            }

        }).start();

        result.put("customerInformation", customerInformationList.size());      //客户信息数据量（int）
        result.put("logisticsInformation", logisticsInformationList.size());    //提单信息数据量（int）
        result.put("loadingTable", loadingTableList.size());                    //装货表数据量（int）
        result.put("unloadingTable", unloadingTableList.size());                //卸货表数据量（int）
        result.put("errorList", errorList);                                     //错误信息（List<String>）
        return result;
    }


}
