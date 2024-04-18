package com.data.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LogisticsInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LogisticsInformationMapper extends BaseMapper<LogisticsInformation> {
    /**
     * 删除表
     * */
    @Update("DROP TABLE IF EXISTS logistics_information;")
    int dropTable();

    /**
     * 创建表
     * */
    @Update("CREATE TABLE logistics_information\n" +
            "(\n" +
            "    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',\n" +
            "    logistics_id VARCHAR(255) COMMENT '提单号',\n" +
            "    \"owner\"        VARCHAR(255) COMMENT '所有者',\n" +
            "    owner_id     VARCHAR(255) COMMENT '所有者ID',\n" +
            "    company_id   VARCHAR(255) COMMENT '公司ID',\n" +
            "    container_id VARCHAR(255) COMMENT '集装箱ID',\n" +
            "    goods_name   VARCHAR(255) COMMENT '货物名称',\n" +
            "    weight       INT COMMENT '重量（单位：kg）',\n" +
            "    remark       VARCHAR(255) COMMENT '备注'\n" +
            ");")
    int createTable();

    /**
     * 批量插入
     * */
    int batchInsert(List<LogisticsInformation> list);
}
