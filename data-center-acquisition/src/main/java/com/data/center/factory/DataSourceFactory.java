package com.data.center.factory;

import com.data.center.pojo.Do.DataSourceDo;

public class DataSourceFactory {


    public static DataSource build(DataSourceDo d) {
        switch (d.getType()) {
            case "mysql":
                return new MysqlDataSource(d.getId(), d.getName(), d.getUrl(), d.getUsername(), d.getPassword(), d.getDbName(), d.getDataAndType());
            case "minio":
                return new MinioDataSource(d.getId(), d.getName(), d.getUrl(), d.getUsername(), d.getPassword(), d.getBucketName(), d.getDataAndType());

            default:
                return null;
        }
    }

}
