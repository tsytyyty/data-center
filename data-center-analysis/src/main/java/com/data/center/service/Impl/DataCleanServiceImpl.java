package com.data.center.service.Impl;

import com.data.center.data.AfterCleanData;
import com.data.center.data.OriginalData;
import com.data.center.contant.RedisConstant;
import com.data.center.service.DataCleanService;
import com.data.center.service.SynchronizationService;
import com.data.center.utils.IdCardUtil;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DataCleanServiceImpl implements DataCleanService, OriginalData, AfterCleanData, RedisConstant {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    @Qualifier("cleanExecutor")
    private ThreadPoolTaskExecutor threadPool;
    @Autowired
    private SynchronizationService synchronizationService;

    @Override
    public void dataClean() throws ExecutionException, InterruptedException {

        customerInformationClean().run();
        logisticsInformationClean().run();
        Future<?> submit1 = threadPool.submit(loadingTableClean());
        Future<?> submit2 = threadPool.submit(unloadingTableClean());
        submit1.get();
        submit2.get();
        // 同步数据到数据库
        synchronizationService.synchronizationAfterClean();

    }

    //客户信息过滤
    private Runnable customerInformationClean() {
        return () -> {
            List<String> remark = new ArrayList<>();
            RMap<Object, Object> map = redissonClient.getMap(DIRTY_CUSTOMER_INFORMATION);       //redis缓存
            map.clear();
            RAtomicLong atomicLong = redissonClient.getAtomicLong(AUTO_CUSTOMER_INFORMATION);   //自增id
            if (data_customer_information.size() != 0) {
                data_customer_information.forEach(list -> {
                    list.forEach(c -> {
                        remark.clear();
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
                        }else {
                            c.setRemark(remark.stream().collect(Collectors.joining(",")));
                            c.setId(atomicLong.incrementAndGet());
                            dirty_customer_information.add(c);
                            map.put(c.getId(), c);
                        }
                    });
                });
            }
            data_customer_information.clear();
        };
    }

    //提单信息过滤
    private Runnable logisticsInformationClean() {
        return () -> {
            List<String> remark = new ArrayList<>();
            RMap<Object, Object> map = redissonClient.getMap(DIRTY_LOGISTICS_INFORMATION);       //redis缓存
            map.clear();
            RAtomicLong atomicLong = redissonClient.getAtomicLong(AUTO_LOGISTICS_INFORMATION);   //自增id
            if (data_logistics_information.size() != 0) {
                data_logistics_information.forEach(list -> {
                    list.forEach(l -> {
                        remark.clear();
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
                        }else {
                            l.setRemark(remark.stream().collect(Collectors.joining(",")));
                            l.setId(atomicLong.incrementAndGet());
                            dirty_logistics_information.add(l);
                            map.put(l.getId(), l);
                        }
                    });
                });
            }
            data_logistics_information.clear();
        };
    }

    //装货表过滤
    private Runnable loadingTableClean() {
        return () -> {
            List<String> remark = new ArrayList<>();
            RMap<Object, Object> map = redissonClient.getMap(DIRTY_LOADING_TABLE);       //redis缓存
            map.clear();
            RAtomicLong atomicLong = redissonClient.getAtomicLong(AUTO_LOADING_TABLE);   //自增id
            if (data_loading_table.size() != 0) {
                data_loading_table.forEach(list -> {
                    list.forEach(l -> {
                        remark.clear();
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
                        }else {
                            l.setRemark(remark.stream().collect(Collectors.joining(",")));
                            l.setId(atomicLong.incrementAndGet());
                            dirty_loading_table.add(l);
                            map.put(l.getId(), l);
                        }
                    });
               });
            }
            data_loading_table.clear();
       };
   }

    //卸货表过滤
    private Runnable unloadingTableClean() {
        return () -> {
            List<String> remark = new ArrayList<>();
            RMap<Object, Object> map = redissonClient.getMap(DIRTY_UNLOADING_TABLE);       //redis缓存
            map.clear();
            RAtomicLong atomicLong = redissonClient.getAtomicLong(AUTO_UNLOADING_TABLE);   //自增id
            if (data_unloading_table.size() != 0) {
                data_unloading_table.forEach(list -> {
                    list.forEach(l -> {
                        remark.clear();
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
                        }else {
                            l.setRemark(remark.stream().collect(Collectors.joining(",")));
                            l.setId(atomicLong.incrementAndGet());
                            dirty_unloading_table.add(l);
                            map.put(l.getId(), l);
                        }
                    });
                });
            }
            data_unloading_table.clear();
        };
    }


}
