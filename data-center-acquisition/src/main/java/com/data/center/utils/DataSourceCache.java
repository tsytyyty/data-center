package com.data.center.utils;

import com.data.center.domain.dto.DataSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DataSourceCache implements Constant {

    private volatile static DataSourceCache instance;   //volatile防指令重排

    //单例模式-懒加载
    public static DataSourceCache getInstance() {
        if (instance == null) {                         //双重判断优化效率
            synchronized (DataSourceCache.class) {
                if (instance == null) {
                    instance = new DataSourceCache();
                }
            }
        }
        return instance;
    }

    //核心缓存对象
    private Cache<Integer, Object> dataSourceCache;

    private DataSourceCache() {
        // 创建一个最大存储100个数据源连接对象的缓存，可以根据需要调整缓存的参数
        this.dataSourceCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)     //10min后过期
                .build();
    }

    public Object getConnection(DataSource dataSource) throws SQLException, ClassNotFoundException, InterruptedException {
        // 尝试从缓存中获取数据源连接对象
        Object connection = dataSourceCache.getIfPresent(dataSource.getHashCode());
        if (connection == null) {
            // 如果缓存中不存在，根据名称创建新的数据源连接对象，并放入缓存中
            connection = dataSource.getConnection(DATA_SOURCE_CONNECT_TIMEOUT);
            dataSourceCache.put(dataSource.getHashCode(), connection);
        }
        return connection;
    }

    public void putConnection(Integer dataSourceName, Object connection) {
        dataSourceCache.put(dataSourceName, connection);
    }


}
