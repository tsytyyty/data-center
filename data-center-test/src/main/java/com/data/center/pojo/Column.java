package com.data.center.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    //字段名
    private String columnName;

    //字段类型
    private String columnType;

    //字段内容
    private Object columnValue;

}
