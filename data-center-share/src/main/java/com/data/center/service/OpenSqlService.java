package com.data.center.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OpenSqlService {


    Connection createConnection();

    /**
     * 查询操作
     */
    List<Map<String, Object>> selectData(String username, String sql) throws SQLException;

    /**
     * 根据用户查询的字段建立数据库表
     * 成功：0
     * 失败：500
     */
    int saveData(String username);

}
