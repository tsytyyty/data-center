package com.data.center.service.Impl;

import com.data.center.pojo.Column;
import com.data.center.pool.ConnectionPool;
import com.data.center.service.OpenDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenDataServiceImpl implements OpenDataService {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/a?useSSL=true&useUnicode=true&cerEncoding=utf8";
    private String username = "root";
    private String password = "123456";

    @Autowired
    private ConnectionPool connectionPool;

    /**
     * 创建数据库连接
     *
     * @return
     */
    public Connection createConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 查询操作
     */
    public List<Column> query(String sql) throws SQLException {

        Connection connection = connectionPool.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        List<Column> listVo = new ArrayList<>();
        try {
            //获取列集
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列的数量
            int columnCount = metaData.getColumnCount();
            //列集合
            Map<String, String> columns = new HashMap<>();
            //遍历出列名
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                String columnType = getColumnType(metaData.getColumnTypeName(i + 1));
                columns.put(columnName, columnType);
            }

            //遍历查询内容添加至list
            while (resultSet.next()) {
                Map<String, Object> map1 = new HashMap<>();
                for (String columnName : columns.keySet()) {
                    Column column = new Column(columnName, columns.get(columnName), resultSet.getString(columnName));
                    listVo.add(column);
                }
            }
            return listVo;
        } finally {
            connectionPool.release(connection);
            stmt.close();
            resultSet.close();
        }
    }

    /**
     * 获取字段类型
     */
    private static String getColumnType(String columnTypeName) {
        if ("VARCHAR".equals(columnTypeName)) {
            return "VARCHAR(255)";
        } else if ("INT".equals(columnTypeName)) {
            return "INT";
        } else if ("DATE".equals(columnTypeName)) {
            return "DATE";
        } else {
            return "VARCHAR(255)";
        }
    }

}