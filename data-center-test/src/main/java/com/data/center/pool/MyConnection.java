//package com.data.center.pool;
//
//import dm.jdbc.desc.conf.DmProperties;
//import dm.jdbc.driver.DmdbConnection;
//import jakarta.annotation.Resource;
//
//import java.sql.*;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//
//
//public class MyConnection extends DmdbConnection implements AutoCloseable {
//
//    @Resource
//    private ConnectionPool connectionPool;
//
//    //调用时间
//    private long callTime;
//
//    public long getCallTime() {
//        return callTime;
//    }
//
//    public void setCallTime(long callTime) {
//        this.callTime = callTime;
//    }
//
//    public MyConnection(DmProperties props) throws SQLException {
//        super(props);
//        this.callTime = System.currentTimeMillis();
//    }
//
//    @Override
//    public void close() {
//        System.out.println("close connection");
//        connectionPool.release(this);
//    }
//}
