//package com.data.center.utils;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DbUtil {
//    private static String driver = "dm.jdbc.driver.DmDriver";
//    private static String url = "jdbc:dm://192.168.1.210:5236/data_center_analysis?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
//    private static String username = "SYSDBA";
//    private static String password = "SYSDBA";
//
//    /**
//     * 创建数据库连接
//     *
//     * @return
//     */
//    public static Connection createConnection() {
//        Connection connection = null;
//        try {
//            Class.forName(driver);
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }
//}
