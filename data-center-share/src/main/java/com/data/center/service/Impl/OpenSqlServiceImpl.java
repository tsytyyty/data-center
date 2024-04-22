package com.data.center.service.Impl;

import com.data.center.contant.RedisConstant;
import com.data.center.mapper.OpenSqlMapper;
import com.data.center.pojo.Column;
import com.data.center.pool.ConnectionPool;
import com.data.center.service.OpenSqlService;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class OpenSqlServiceImpl implements OpenSqlService, RedisConstant {

//    @Value("${connection.pool.driver}")
//    private String driver;
//    @Value("${connection.pool.url}")
//    private String url;
//    @Value("${connection.pool.username}")
//    private String username;
//    @Value("${connection.pool.password}")
//    private String password;



    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ConnectionPool connectionPool;
    @Autowired
    private OpenSqlMapper openSqlMapper;


    private static Map<String, List<Map<String, Column>>> selectResults = new ConcurrentHashMap<>();




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

    @Override
    public void exportData(HttpServletResponse response, String username) {
        List<Map<String, Column>> list = selectResults.get(username);
//        List<Map<String, Column>> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++){
//            Map<String, Column> map = new HashMap<>();
//            map.put("username", new Column("username", "VARCHAR(255)", "yty"));
//            map.put("password", new Column("password", "VARCHAR(255)", "123456"));
//            list.add(map);
//        }

        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);
        AtomicInteger i = new AtomicInteger(0);
        Row finalRow = sheet.createRow(0);
        list.get(0).forEach((columnName, column) -> {
            Cell cell = finalRow.createCell(i.get());
            cell.setCellType(CellType.STRING);
            cell.setCellValue(columnName);
            i.incrementAndGet();
        });
        for (int j = 0; j < list.size(); j++) {
            Row row = sheet.createRow(j + 1);
            Map<String, Column> map = list.get(j);
            i.set(0);
            map.forEach((columnName, column) -> {
                Cell cell = row.createCell(i.get());
                cell.setCellType(CellType.STRING);
                cell.setCellValue((String) column.getColumnValue());
                i.incrementAndGet();
            });
        }

        response.setContentType("application/octet-stream");
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName="+ username + "_" + UUID.randomUUID().toString() +".xlsx");

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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