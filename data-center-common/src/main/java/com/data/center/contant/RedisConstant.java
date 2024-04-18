package com.data.center.contant;

public interface RedisConstant {

    //错误数据
    String DIRTY_CUSTOMER_INFORMATION = "dirty:customer:information";
    String DIRTY_LOGISTICS_INFORMATION = "dirty:logistics:information";
    String DIRTY_LOADING_TABLE = "dirty:loading:table";
    String DIRTY_UNLOADING_TABLE = "dirty:unloading:table";
    //错误数据自增id
    String AUTO_CUSTOMER_INFORMATION = "auto:customer:information";
    String AUTO_LOGISTICS_INFORMATION = "auto:logistics:information";
    String AUTO_LOADING_TABLE = "auto:loading:table";
    String AUTO_UNLOADING_TABLE = "auto:unloading:table";

    //分布式锁id
    String LOCK_TRANSMIT_CLEAN = "data_transmit_and_clean";
    String LOCK_READ_AND_WRITE = "read_and_write";

}
