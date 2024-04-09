package com.data.center.service;

import com.data.center.pojo.Column;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface OpenDataService {

    Connection createConnection();

    List<Column> query(String sql) throws SQLException;

}
