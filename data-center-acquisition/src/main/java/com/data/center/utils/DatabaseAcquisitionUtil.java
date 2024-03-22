package com.data.center.utils;

import com.data.center.domain.Do.CustomerInformation;
import com.data.center.domain.Do.LoadingTable;
import com.data.center.domain.Do.LogisticsInformation;
import com.data.center.domain.Do.UnloadingTable;
import com.data.center.domain.dto.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class DatabaseAcquisitionUtil {

    /**
     * 对单个数据源执行数据采集
     */
    public static Map<String, List<T>> acquisitionToMap(Connection con, String dbName, Map<String, String> dbTable){
        Map<String, List<T>> map = new HashMap<>();
        PreparedStatement p = null;
        ResultSet resultSet = null;
        dbTable.forEach((tableName, tableType) -> {
            switch (tableType){
                case "LoadingTable":
                    List<T> loadingTableList = loadingTableData(con, dbName, tableName, p, resultSet);
                    map.put(tableName, loadingTableList);
                    break;
                case "UnloadingTable":
                    List<T> unloadingTableList = unloadingTableData(con, dbName, tableName, p, resultSet);
                    map.put(tableName, unloadingTableList);
                    break;
                case "CustomerInformation":
                    List<T> customerInformationList = customerInformationData(con, dbName, tableName, p, resultSet);
                    map.put(tableName, customerInformationList);
                    break;
                case "LogisticsInformation":
                    List<T> logisticsInformationList = logisticsInformationData(con, dbName, tableName, p, resultSet);
                    map.put(tableName, logisticsInformationList);
                    break;
            }
        });
        return map;
    }

    /**
     * 装货表数据采集
     */
    public static <T> List<T> loadingTableData(Connection con, String dbName, String tableName, PreparedStatement p, ResultSet resultSet){
        List<LoadingTable> list = new ArrayList<>();
        String sql = "select " + getColumn("LoadingTable") + " from " + dbName + "." + tableName;
        try {
             resultSet = p.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage() + e);
        }
        try {
            while (resultSet.next()){
                LoadingTable loadingTable = new LoadingTable(
                        0,
                        resultSet.getString("ship_companies"),
                        resultSet.getString("ship_name"),
                        resultSet.getDate("work_begin_time"),
                        resultSet.getDate("work_end_time"),
                        resultSet.getDate("departure_time"),
                        resultSet.getDate("arrive_time"),
                        resultSet.getString("port"),
                        resultSet.getString("logistics_id"),
                        resultSet.getString("container_id"),
                        resultSet.getInt("container_size"),
                        resultSet.getString("departure_place"),
                        resultSet.getString("destination_place"),
                        null
                );
                list.add(loadingTable);
            }
        }catch (SQLException e){
            log.error("采集数据异常：{}数据库-{}数据表-LoadingTable数据类型", dbName, tableName);
            log.error(e.getMessage() + e);
        }
        return (List<T>) list;
    }

    /**
     * 卸货表数据采集
     */
    public static <T> List<T> unloadingTableData(Connection con, String dbName, String tableName, PreparedStatement p, ResultSet resultSet) {
        List<UnloadingTable> list = new ArrayList<>();
        String sql = "select " + getColumn("UnloadingTable") + " from " + dbName + "." + tableName;
        try {
            resultSet = p.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage() + e);
        }
        try {
            while (resultSet.next()){
                UnloadingTable unloadingTable = new UnloadingTable(
                        0,
                        resultSet.getString("ship_companies"),
                        resultSet.getString("ship_name"),
                        resultSet.getDate("work_begin_time"),
                        resultSet.getDate("work_end_time"),
                        resultSet.getDate("departure_time"),
                        resultSet.getDate("arrive_time"),
                        resultSet.getString("port"),
                        resultSet.getString("logistics_id"),
                        resultSet.getString("container_id"),
                        resultSet.getInt("container_size"),
                        resultSet.getString("departure_place"),
                        resultSet.getString("destination_place"),
                        null
                );
                list.add(unloadingTable);
            }
        }catch (SQLException e){
            log.error("采集数据异常：{}数据库-{}数据表-UnloadingTable数据类型", dbName, tableName);
            log.error(e.getMessage() + e);
        }
        return (List<T>) list;
    }

    /**
     * 提单信息数据采集
     */
    public static <T> List<T> logisticsInformationData(Connection con, String dbName, String tableName, PreparedStatement p, ResultSet resultSet) {
        List<LogisticsInformation> list = new ArrayList<>();
        String sql = "select " + getColumn("LogisticsInformation") + " from " + dbName + "." + tableName;
        try {
            resultSet = p.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage() + e);
        }
        try {
            while (resultSet.next()){
                LogisticsInformation logisticsInformation = new LogisticsInformation(
                        0,
                        resultSet.getString("logistics_id"),
                        resultSet.getString("owner"),
                        resultSet.getString("owner_id"),
                        resultSet.getString("company_id"),
                        resultSet.getString("container_id"),
                        resultSet.getString("goods_name"),
                        resultSet.getInt("weight"),
                        null
                );
                list.add(logisticsInformation);
            }
        }catch (SQLException e){
            log.error("采集数据异常：{}数据库-{}数据表-LogisticsInformation数据类型", dbName, tableName);
            log.error(e.getMessage() + e);
        }
        return (List<T>) list;
    }

    /**
     * 客户信息数据采集
     */
    public static <T> List<T> customerInformationData(Connection con, String dbName, String tableName, PreparedStatement p, ResultSet resultSet) {
        List<CustomerInformation> list = new ArrayList<>();
        String sql = "select " + getColumn("CustomerInformation") + " from " + dbName + "." + tableName;
        try {
            resultSet = p.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage() + e);
        }
        try {
            while (resultSet.next()){
                CustomerInformation customerInformation = new CustomerInformation(
                        0,
                        resultSet.getString("name"),
                        resultSet.getString("id_card"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        null
                );
                list.add(customerInformation);
            }
        }catch (SQLException e){
            log.error("采集数据异常：{}数据库-{}数据表-CustomerInformation数据类型", dbName, tableName);
            log.error(e.getMessage() + e);
        }
        return (List<T>) list;
    }


    /**
     * 获取数据表列名
     */
    private static String getColumn(String tableType){
        switch (tableType){
            case "LoadingTable":
            case "UnloadingTable":
                return "id, arrive_time, container_id, ship_companies, departure_time, container_size, departure_place, ship_name, work_begin_time, port, remark, work_end_time, logistics_id, destination_place";
            case "LogisticsInformation":
                return "id, logistics_id, owner, owner_id, company_id, container_id, goods_name, weight";
            case "CustomerInformation":
                return "id, name, id_card, phone_number, address";
            default:
                return null;
        }
    }

}
