package com.data.center.factory;

import com.data.center.contant.AcquisitionConstant;
import com.data.center.factory.DataSource;
import com.data.center.utils.DataSourceCache;
import com.data.center.utils.AcquisitionDatabaseUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysqlDataSource implements DataSource, AcquisitionConstant {

    //id
    private String id;

    //名称
    private String name;

    //数据库地址
    private String url;

    //数据库用户名
    private String username;

    //数据库密码
    private String password;

    //数据库名称
    private String dbName;

    //数据表->数据类型
    Map<String, String> dbTable;


    @Override
    public Connection getConnection(long timeOut) throws SQLException {
        long begin = System.currentTimeMillis();
        try {
            Class.forName(DATA_SOURCE_DRIVER_MYSQL);
        }catch (ClassNotFoundException e){
            log.error(e.getMessage(), e);
        }

        Connection connection = null;
        // 循环尝试连接，直到连接成功或超时
        while (connection == null) {
            try {
                // 尝试建立连接
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                // 检查是否超时
                if (System.currentTimeMillis() - begin > timeOut) {
                    // 已超时，抛出异常
                    throw new SQLException("mysql——尝试与{}建立时出现错误连接超时......", url);
                }
                // 未超时，等待一段时间后继续尝试连接
                try {
                    Thread.sleep(100); // 每次尝试间隔100毫秒
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return connection;
    }

    @Override
    public int testConnect() throws Exception {
        Class.forName(DATA_SOURCE_DRIVER_MYSQL);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);//用参数得到连接对象
            if (!conn.isValid(2000)){
                throw new Exception("mysql——尝试与" + url + "建立时出现错误连接超时......");
            }
        } catch (Exception e) {
            //连接测试失败
            log.error(e.getMessage(), e);
            return 500;
        }
        //将连接放入缓存
        DataSourceCache.getInstance().putConnection(id, conn);
        return 200;
    }

    @Override
    public Map<String, Object> testConnectBeforeAdd() throws ClassNotFoundException, SQLException {
        Map<String, Object> map = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        Class.forName(DATA_SOURCE_DRIVER_MYSQL);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            if (!conn.isValid(2000)){
                throw new Exception("mysql——尝试与{}建立时出现错误连接超时......");
            }
        } catch (Exception e) {
            //连接测试失败
            log.error(e.getMessage(), e);
            map.put("code", 500);
            return map;
        }
        //检查数据源信息中文件是否存在
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("show tables from " + dbName + ";");
        Set<String> dbTableSet = new HashSet<>();

        while(rs.next()) {
            dbTableSet.add(rs.getString(1));
        }
        stmt.close();
        rs.close();
        dbTable.keySet().forEach(f -> {
            if (!dbTableSet.contains(f)) {
                errorList.add(f);
            }
        });
        if (errorList.size() > 0) {
            map.put("code", 404);
            map.put("errorList", errorList);
            return map;
        }
        map.put("code", 200);
        map.put("connection",conn);
        return map;
    }

//    @Override
//    public Map<String, Object> testConnectBeforeUpdate() throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        List<String> errorList = new ArrayList<>();
//        Class.forName(DATA_SOURCE_DRIVER_MYSQL);
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(url, username, password);//用参数得到连接对象
//            if (!conn.isValid(2000)){
//                throw new Exception("mysql——尝试与{}建立时出现错误连接超时......");
//            }
//        } catch (Exception e) {
//            //连接测试失败
//            log.error(e.getMessage(), e);
//            map.put("code", 500);
//            return map;
//        }
//        //检查数据源信息中文件是否存在
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("Show tables;");
//        Set<String> dbTableSet = new HashSet<>();
//
//        while(rs.next()) {
//            dbTableSet.add(rs.getString(1));
//        }
//        stmt.close();
//        rs.close();
//        dbTable.keySet().forEach(f -> {
//            if (!dbTableSet.contains(f)) {
//                errorList.add(f);
//            }
//        });
//        if (errorList.size() > 0) {
//            map.put("code", 404);
//            map.put("errorList", errorList);
//            return map;
//        }
//        map.put("code", 200);
//        map.put("connection",conn);
//        return map;
//    }

    @Override
    public Callable<Map<String, List<Object>>> dataAcquisition() {
        return () -> {
            DataSourceCache cache = DataSourceCache.getInstance();
            Connection connection = (Connection) cache.getConnection(this);
            if (!connection.isValid(2000)) {
                connection = getConnection(DATA_SOURCE_CONNECT_TIMEOUT);
                DataSourceCache.getInstance().putConnection(getId(), connection);
            }
            Map<String, List<Object>> map = AcquisitionDatabaseUtil.acquisitionToMap(name, connection, dbName, dbTable);
            return map;
        };
    }

    @Override
    public String getUUID() {
        return getId();
    }
}

