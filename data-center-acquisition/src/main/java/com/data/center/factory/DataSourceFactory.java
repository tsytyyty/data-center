package com.data.center.factory;

import com.data.center.domain.Do.MysqlDataSource;
import com.data.center.domain.dto.DataSource;

public class DataSourceFactory {

    public static DataSource getDataSource(String name) {
        switch (name) {
            case "mysql":
                return new MysqlDataSource();
            default:
                return null;
        }
    }

}
