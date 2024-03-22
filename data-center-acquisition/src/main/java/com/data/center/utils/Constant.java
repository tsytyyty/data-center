package com.data.center.utils;

public interface Constant {

    /**
     * 数据源类型
     */
    String DATA_SOURCE_TYPE_MYSQL = "mysql";
    String DATA_SOURCE_TYPE_ORACLE = "oracle";
    String DATA_SOURCE_TYPE_DM = "dm";
    String DATA_SOURCE_TYPE_MINIO = "minio";
    String DATA_SOURCE_TYPE_HDFS = "hdfs";

    /**
     * 数据库驱动
     */
    String DATA_SOURCE_DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";
    String DATA_SOURCE_DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    String DATA_SOURCE_DRIVER_DM = "dm.jdbc.driver.DmDriver";

    /**
     * 数据源连接超时时间
     */
    int DATA_SOURCE_CONNECT_TIMEOUT = 10000;

}
