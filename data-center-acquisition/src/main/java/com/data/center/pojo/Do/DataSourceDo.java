package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="data_source", autoResultMap = true)
public class DataSourceDo implements Serializable {
    /**
     * 主键，数据源ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * key——value
     * database：数据表（minio：文件名）—— 类型（CustomerInformation、LogisticsInformation、LoadingTable、UnloadingTable）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> dataAndType;

    /**
     * 数据源类型
     * minio，mysql小写
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}