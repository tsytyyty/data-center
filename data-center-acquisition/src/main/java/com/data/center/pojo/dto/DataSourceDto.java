package com.data.center.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceDto implements Serializable {
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
     *
     * "dataAndType": [
     *         "客户信息2021.xlsx_CustomerInformation",
     *         "物流信息2021.txt_LogisticsInformation",
     *         "装货表2021.txt_LoadingTable",
     *         "卸货表2021.txt_UnloadingTable"
     * ],
     */
    private List<String> dataAndType;

    /**
     * 数据源类型
     * minio，mysql小写
     */
    private String type;

}