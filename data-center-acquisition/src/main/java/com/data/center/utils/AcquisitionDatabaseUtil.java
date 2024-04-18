package com.data.center.utils;

import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class AcquisitionDatabaseUtil {

    /**
     * 对单个数据源执行数据采集
     */
    public static Map<String, List<Object>> acquisitionToMap(String name, Connection con, String dbName, Map<String, String> dbTable) {
        Map<String, List<Object>> map = new HashMap<>();
        List<Object> errorList = new ArrayList<>();
        ResultSet resultSet = null;
        dbTable.forEach((tableName, tableType) -> {
            try {
                switch (tableType) {
                    case "LoadingTable":
                        List<Object> loadingTableList = loadingTableData(con, dbName, tableName, resultSet);
                        map.put(tableName, loadingTableList);
                        break;
                    case "UnloadingTable":
                        List<Object> unloadingTableList = unloadingTableData(con, dbName, tableName, resultSet);
                        map.put(tableName, unloadingTableList);
                        break;
                    case "CustomerInformation":
                        List<Object> customerInformationList = customerInformationData(con, dbName, tableName, resultSet);
                        map.put(tableName, customerInformationList);
                        break;
                    case "LogisticsInformation":
                        List<Object> logisticsInformationList = logisticsInformationData(con, dbName, tableName, resultSet);
                        map.put(tableName, logisticsInformationList);
                        break;
                }
            } catch (SQLException e) {
                log.error(e.getMessage() + e);
                errorList.add("采集数据源出现错误，请检查数据源！ 名称：" + name + "，数据库：" + dbName + "，表：" + tableName);
            }
        });
        return map;
    }

    /**
     * 装货表数据采集
     */
    public static <T> List<T> loadingTableData(Connection con, String dbName, String tableName, ResultSet resultSet) throws SQLException {
        List<LoadingTable> list = new ArrayList<>();
        String sql = "select  * " + " from " + dbName + "." + tableName;
        resultSet = con.prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            LoadingTable loadingTable = new LoadingTable(
                    0,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDate(5),
                    resultSet.getDate(6),
                    resultSet.getDate(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getString(10),
                    resultSet.getInt(11),
                    resultSet.getString(12),
                    resultSet.getString(13),
                    null
            );
            list.add(loadingTable);
        }
        return (List<T>) list;
    }

    /**
     * 卸货表数据采集
     */
    public static <T> List<T> unloadingTableData(Connection con, String dbName, String tableName, ResultSet resultSet) throws SQLException {
        List<UnloadingTable> list = new ArrayList<>();
        String sql = "select  * " + " from " + dbName + "." + tableName;
        resultSet = con.prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            UnloadingTable unloadingTable = new UnloadingTable(
                    0,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDate(5),
                    resultSet.getDate(6),
                    resultSet.getDate(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getString(10),
                    resultSet.getInt(11),
                    resultSet.getString(12),
                    resultSet.getString(13),
                    null
            );
            list.add(unloadingTable);
        }
        return (List<T>) list;
    }

    /**
     * 提单信息数据采集
     */
    public static <T> List<T> logisticsInformationData(Connection con, String dbName, String tableName, ResultSet resultSet) throws SQLException {
        List<LogisticsInformation> list = new ArrayList<>();
        String sql = "select  * " + " from " + dbName + "." + tableName;
        resultSet = con.prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            LogisticsInformation logisticsInformation = new LogisticsInformation(
                    0,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getInt(8),
                    null
            );
            list.add(logisticsInformation);
        }
        return (List<T>) list;
    }

    /**
     * 客户信息数据采集
     */
    public static <T> List<T> customerInformationData(Connection con, String dbName, String tableName, ResultSet resultSet) throws SQLException {
        List<CustomerInformation> list = new ArrayList<>();
        String sql = "select  * " + " from " + dbName + "." + tableName;
        resultSet = con.prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            CustomerInformation customerInformation = new CustomerInformation(
                    0,
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    null
            );
            list.add(customerInformation);
        }
        return (List<T>) list;
    }


}
