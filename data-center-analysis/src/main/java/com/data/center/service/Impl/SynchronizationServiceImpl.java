package com.data.center.service.Impl;

import com.data.center.data.AfterAnalysisData;
import com.data.center.data.AfterCleanData;
import com.data.center.contant.RedisConstant;
import com.data.center.mapper.*;
import com.data.center.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SynchronizationServiceImpl implements SynchronizationService, AfterCleanData, AfterAnalysisData, RedisConstant {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CustomerInformationMapper customerInformationMapper;
    @Autowired
    private LogisticsInformationMapper logisticsInformationMapper;
    @Autowired
    private LoadingTableMapper loadingTableMapper;
    @Autowired
    private UnloadingTableMapper unloadingTableMapper;
    @Autowired
    private ResultMapper resultMapper;

    @Async
    public void synchronizationAfterClean(){
        long begin = System.currentTimeMillis();
        //加读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock(LOCK_READ_AND_WRITE);
        lock.writeLock().lock();
        try {
            List<Future<Void>> list = new ArrayList<>();
            list.add(customerInformation());
            list.add(logisticsInformation());
            list.add(loadingTable());
            list.add(unloadingTable());
            //等待所有线程执行完成
            list.forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //清空缓存
            clean_customer_information.clear();
            clean_logistics_information.clear();
        }finally {
            lock.writeLock().unlock();
            log.debug("同步数据完成，耗时：" + (System.currentTimeMillis() - begin) + "ms");
        }
    }

    @Async
    public void synchronizationAfterAnalysis() {
        //加读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock(LOCK_READ_AND_WRITE);
        try {
            //删除旧信息
            deleteTable().get();
            List<Future<Void>> list = new ArrayList<>();
            list.add(tabulateData());
            list.add(portThroughput());
            list.add(portGoodsThroughput());
            list.add(goodsThroughput());
            list.add(portGoodsInput());
            list.add(portGoodsOutput());
            list.add(yoyQoqEveryYear());
            list.add(portGoodsFlowTime());
            list.forEach(future -> {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 清洗后数据同步
     */
    @Async
    public Future<Void> customerInformation(){
        customerInformationMapper.dropTable();
        customerInformationMapper.createTable();
        for (int i = 0; i <= clean_customer_information.size(); i += 3000){
            if (i + 3000 >= clean_customer_information.size()){
                customerInformationMapper.batchInsert(clean_customer_information.subList(i, clean_customer_information.size()));
                break;
            }
            customerInformationMapper.batchInsert(clean_customer_information.subList(i, i + 3000));
        }
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> logisticsInformation(){
        logisticsInformationMapper.dropTable();
        logisticsInformationMapper.createTable();
        for (int i = 0; i <= clean_logistics_information.size(); i += 3000){
            if (i + 3000 >= clean_logistics_information.size()){
                logisticsInformationMapper.batchInsert(clean_logistics_information.subList(i, clean_logistics_information.size()));
                break;
            }
            logisticsInformationMapper.batchInsert(clean_logistics_information.subList(i, i + 3000));
        }
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> loadingTable(){
        loadingTableMapper.dropTable();
        loadingTableMapper.createTable();
        for (int i = 0; i <= clean_loading_table.size(); i += 2000){
            if (i + 2000 >= clean_loading_table.size()){
                loadingTableMapper.batchInsert(clean_loading_table.subList(i, clean_loading_table.size()));
                break;
            }
            loadingTableMapper.batchInsert(clean_loading_table.subList(i, i + 2000));
        }
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> unloadingTable(){
        unloadingTableMapper.dropTable();
        unloadingTableMapper.createTable();
        for (int i = 0; i <= clean_unloading_table.size(); i += 2000){
            if (i + 2000 >= clean_unloading_table.size()){
                unloadingTableMapper.batchInsert(clean_unloading_table.subList(i, clean_unloading_table.size()));
                break;
            }
            unloadingTableMapper.batchInsert(clean_unloading_table.subList(i, i + 2000));
        }
        return new AsyncResult<>(null);
    }


    /**
     * 分析后数据同步
     */
    @Async
    public Future<Void> tabulateData(){
        resultMapper.tabulateData(totalData);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> portThroughput(){
        resultMapper.portThroughput(portThroughputList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> goodsThroughput(){
        resultMapper.goodsThroughput(goodsThroughputList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> portGoodsThroughput(){
        resultMapper.portGoodsThroughput(portGoodsThroughputList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> portGoodsInput(){
        resultMapper.portGoodsInput(portGoodsInputList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> portGoodsOutput(){
        resultMapper.portGoodsOutput(portGoodsOutputList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> yoyQoqEveryYear(){
        resultMapper.yoyQoqEveryYear(portYoyQoqList);
        return new AsyncResult<>(null);
    }
    @Async
    public Future<Void> portGoodsFlowTime(){
        resultMapper.portGoodsFlowTime(portGoodsFlowTimeList);
        return new AsyncResult<>(null);
    }

    /**
     * 删除表
     */
    @Async
    public Future<Void> deleteTable(){
        resultMapper.deleteTable("tabulate_data");
        resultMapper.deleteTable("port_throughput");
        resultMapper.deleteTable("goods_throughput");
        resultMapper.deleteTable("port_goods_throughput");
        resultMapper.deleteTable("port_goods_input");
        resultMapper.deleteTable("port_goods_output");
        resultMapper.deleteTable("port_yoy_qoq");
        resultMapper.deleteTable("port_goods_flow_time");
        return new AsyncResult<>(null);
    }


}
