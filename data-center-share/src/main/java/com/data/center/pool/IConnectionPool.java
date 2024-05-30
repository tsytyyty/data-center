package com.data.center.pool;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {

    // 获取连接(重复利用机制)
    Connection getConnection() throws SQLException;

    // 释放连接(可回收机制)
    void release(Connection conn);

    int INITSIZE = 3;
    int MAXSIZE = 10;
    int TIMEOUT = 10000;
    String DIRER = "dm.jdbc.driver.DmDriver";
    String URL = "jdbc:dm://192.168.1.210:5236/data_center_analysis?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
    String USERNAME = "SYSDBA";
    String PASSWORD = "SYSDBA";

}
