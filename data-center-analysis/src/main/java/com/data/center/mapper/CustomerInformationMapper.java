package com.data.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.center.pojo.Do.CustomerInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CustomerInformationMapper extends BaseMapper<CustomerInformation> {

    /**
     * 删除表
     * */
    @Update("DROP TABLE IF EXISTS customer_information;")
    int dropTable();

    /**
     * 创建表
     * */
    @Update("CREATE TABLE customer_information\n" +
            "(\n" +
            "    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',\n" +
            "    name         VARCHAR(255) COMMENT '客户姓名',\n" +
            "    id_card      VARCHAR(255) COMMENT '身份证号码',\n" +
            "    phone_number VARCHAR(255) COMMENT '电话号码',\n" +
            "    address      VARCHAR(255) COMMENT '地址',\n" +
            "    remark       VARCHAR(255) COMMENT '备注'\n" +
            ");")
    int createTable();

    /**
     * 批量插入
     * */
    int batchInsert(List<CustomerInformation> list);

}
