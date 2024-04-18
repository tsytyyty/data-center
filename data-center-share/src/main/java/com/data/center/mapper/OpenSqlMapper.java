package com.data.center.mapper;

import com.data.center.pojo.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpenSqlMapper {

    /**
     * 删除数据库
     */
    int dropDatabase();

    /**
     * 建立数据库
     */
    int createDatabase();

    /**
     * 删除表
     */
    int dropTable(@Param("tableName")String tableName);

    /**
     * 创建表
     */
    int createTable(String tableName, Map<String, String> columns);

    /**
     * 插入数据
     */
    int insertData(String tableName, List<Map<String, Column>> list);
}