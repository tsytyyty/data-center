package com.data.center.factory;

import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.pojo.dto.DataSourceDto;

public class DataSourceFactory {

    public static DataSource getDataSource(String type, DataSourceDto dto) {
        switch (type) {
            case "mysql":
                return new MysqlDataSource(dto.getId(), dto.getName(), dto.getUrl(), dto.getUsername(), dto.getPassword(), dto.getDbName(), dto.getTableName());
            case "minio":
                return new MinioDataSource(dto.getId(), dto.getName(), dto.getUrl(), dto.getUsername(), dto.getPassword(), dto.getDbName(), dto.getTableName());

            default:
                return null;
        }
    }

    public static DataSource getDataSource(DataSourceDo d) {
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
