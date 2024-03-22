package com.data.center.pool;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
    // 获取连接(重复利用机制)
    public Connection getConnection() throws SQLException;

    // 释放连接(可回收机制)
    public  void release(Connection conn);

}
