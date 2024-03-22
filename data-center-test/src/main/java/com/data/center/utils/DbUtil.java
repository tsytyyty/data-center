package com.data.center.utils;

import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DbUtil {

    @Value("${connection.pool.driver}")
    private String driver = "com.mysql.cj.jdbc.Driver";
    @Value("${connection.pool.url}")
    private String url = "jdbc:mysql://localhost:3306/a?useSSL=true&useUnicode=true&cerEncoding=utf8";
    @Value("${connection.pool.username}")
    private String username = "root";
    @Value("${connection.pool.password}")
    private String password = "123456";

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
     * 执行插入操作
     *
     * @param connection
     * @param insertSql
     * @throws SQLException
     */
    public static void executeInsert(Connection connection, String insertSql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}