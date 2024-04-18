package com.data.center.service.Impl;

import com.data.center.contant.RedisConstant;
import com.data.center.mapper.OpenSqlMapper;
import com.data.center.pojo.Column;
import com.data.center.pool.ConnectionPool;
import com.data.center.service.OpenSqlService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Data
public class OpenSqlServiceImpl implements OpenSqlService, RedisConstant {

    @Value("${connection.pool.driver}")
    private String driver;
    @Value("${connection.pool.url}")
    private String url;
    @Value("${connection.pool.username}")
    private String username;
    @Value("${connection.pool.password}")
    private String password;

//    @Value("${connection.pool.driver}")
//    private String driver = "dm.jdbc.driver.DmDriver";
//    @Value("${connection.pool.url}")
//    private String url = "jdbc:dm://localhost:5236/data_center_analysis?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
//    @Value("${connection.pool.username}")
//    private String username = "SYSDBA";
//    @Value("${connection.pool.password}")
//    private String password = "SYSDBA";

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ConnectionPool connectionPool;
    @Autowired
    private OpenSqlMapper openSqlMapper;


    private static Map<String, List<Map<String, Column>>> selectResults = new ConcurrentHashMap<>();

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


    public List<Map<String, Object>> selectData(String username, String sql) throws SQLException {
        RReadWriteLock lock = redissonClient.getReadWriteLock(LOCK_READ_AND_WRITE);
        lock.readLock().lock();
        try {
            Connection connection = connectionPool.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            List<Map<String, Object>> listVo = new ArrayList<>();
            List<Map<String, Column>> list = new ArrayList<>();
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
                    Map<String, Column> map = new HashMap<>();
                    Map<String, Object> mapVo = new HashMap<>();
                    columns.forEach((columnName, columnType) -> {
                        try {
                            Column column = new Column(columnName, columnType, resultSet.getString(columnName));
                            map.put(columnName, column);
                            mapVo.put(columnName, resultSet.getString(columnName));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    listVo.add(mapVo);
                    list.add(map);
                }
                return listVo;
            } finally {
                connectionPool.release(connection);
                stmt.close();
                resultSet.close();
            }
        }finally {
            lock.readLock().unlock();
        }
    }


    @Transactional
    public int saveData(String username){
        List<Map<String, Column>> maps = selectResults.get(username);
        //删除之前建立的数据库表
        openSqlMapper.dropTable(username);
        //判空
        if (maps == null){
            return 500;
        }
        Map<String, Column> map = maps.get(0);
        //将字段名存到list中
        //客户信息 -> VARCHAR(255)
        Map<String, String> columns = new HashMap<>();
        map.forEach((columnName, column) -> {
            columns.put(columnName, column.getColumnType());
        });
        openSqlMapper.createTable(username, columns);
        insertData(username);
        return 200;
    }

    public void insertData(String username){
        openSqlMapper.insertData(username, selectResults.get(username));
    }

    /**
     * 获取字段类型
     */
    private static String getColumnType(String columnTypeName) {
        if ("VARCHAR".equals(columnTypeName) || "varchar".equals(columnTypeName)) {
            return "VARCHAR(255)";
        } else if ("INT".equals(columnTypeName) || "int".equals(columnTypeName)) {
            return "INT";
        } else if ("DATE".equals(columnTypeName) || "date".equals(columnTypeName)) {
            return "DATE";
        } else {
            return "VARCHAR(255)";
        }
    }

}