package com.data.center.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceDto {

    //id
    private int id;

    //名称
    private String name;

    //地址
    private String url;

    //用户名
    private String username;

    //密码
    private String password;

    //文件路径
    private String path;

    //数据库
    private String dbName;

    //表名（文件名）- 文件类型
    Map<String, String> tableName;

    //数据源类型
    private String type;
}
