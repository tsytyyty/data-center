package com.data.center.mapper;

import com.data.center.pojo.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Mapper
@Qualifier("PrimarySqlSessionTemplate")
public interface OpenSqlMapper {

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