package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="data_source")
public class DataSourceDo implements Serializable {
    /**
     * 主键，数据源ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 数据表（文件名）- 类型
     */
    private Map<String,String> dataAndType;

    /**
     * 数据源类型
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}