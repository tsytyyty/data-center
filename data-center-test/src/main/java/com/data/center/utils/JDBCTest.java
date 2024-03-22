//package com.data.center.utils;
//
//import com.alibaba.druid.pool.DruidDataSource;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class JDBCTest {
//    public static void main(String[] args) {
//
//
//        try (Connection connection = DruidConnectionPoll.getInstance().getConnection();
//             PreparedStatement stmt = connection.prepareStatement("SHOW TABLES ");
//             ResultSet resultSet = stmt.executeQuery()
//        ) {
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private static class DruidConnectionPoll{
//        private static volatile DruidConnectionPoll instance;
//        private final DruidDataSource druidDataSource;
//        public DruidConnectionPoll(){
//            // druid连接池
//            druidDataSource = new DruidDataSource();
//            druidDataSource.setUrl("jdbc:mysql://192.168.48.132:3306/test?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=True");
//            druidDataSource.setUsername("root");
//            druidDataSource.setPassword("123456");
//        }
//
//        public static DruidConnectionPoll getInstance() {
//            if (instance == null) {
//                synchronized (DruidConnectionPoll.class) {
//                    if (instance == null) {
//                        instance = new DruidConnectionPoll();
//                    }
//                }
//            }
//            return instance;
//        }
//
//        public Connection getConnection() throws SQLException {
//            return druidDataSource.getConnection();
//        }
//    }
//}
