package com.data.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LoadingTableMapper extends BaseMapper<LoadingTable> {

    /**
     * 删除表
     * */
    @Update("DROP TABLE IF EXISTS loading_table;")
    int dropTable();

    /**
     * 创建表
     * */
    @Update("CREATE TABLE loading_table\n" +
            "(\n" +
            "    id                INT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',\n" +
            "    ship_companies    VARCHAR(255) COMMENT '船公司',\n" +
            "    ship_name         VARCHAR(255) COMMENT '船名称',\n" +
            "    work_begin_time   DATETIME COMMENT '作业开始时间',\n" +
            "    work_end_time     DATETIME COMMENT '作业结束时间',\n" +
            "    departure_time    DATETIME COMMENT '始发时间',\n" +
            "    arrive_time       DATETIME COMMENT '到达时间',\n" +
            "    port              VARCHAR(255) COMMENT '作业港口',\n" +
            "    logistics_id      VARCHAR(255) COMMENT '提单号',\n" +
            "    container_id      VARCHAR(255) COMMENT '集装箱箱号',\n" +
            "    container_size    INT COMMENT '箱尺寸（TEU）',\n" +
            "    departure_place   VARCHAR(255) COMMENT '启运地',\n" +
            "    destination_place VARCHAR(255) COMMENT '目的地',\n" +
            "    remark            VARCHAR(255) COMMENT '备注'\n" +
            ");")
    int createTable();

    /**
     * 批量插入
     * */
    int batchInsert(List<LoadingTable> list);
}
