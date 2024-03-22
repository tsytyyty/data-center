package com.data.center.domain.Do;

import com.data.center.domain.dto.DataSource;
import com.data.center.utils.Constant;
import com.data.center.utils.DataSourceCache;
import com.data.center.utils.DatabaseAcquisitionUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;


@Data
@Slf4j
public class MysqlDataSource implements DataSource, Constant {

    //数据库地址
    private String url;

    //数据库用户名
    private String username;

    //数据库密码
    private String password;

    //数据库名称
    private String dbName;

    //数据表->数据类型
    Map<String, String> dbTable;


    @Override
    public Connection getConnection(long timeOut) throws SQLException {
        long begin = System.currentTimeMillis();
        try {
            Class.forName(DATA_SOURCE_DRIVER_MYSQL);
        }catch (ClassNotFoundException e){
            log.error(e.getMessage(), e);
        }

        Connection connection = null;
        // 循环尝试连接，直到连接成功或超时
        while (connection == null) {
            try {
                // 尝试建立连接
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                // 检查是否超时
                if (System.currentTimeMillis() - begin > timeOut) {
                    // 已超时，抛出异常
                    throw new SQLException("mysql——尝试与{}建立时出现错误连接超时......", url);
                }
                // 未超时，等待一段时间后继续尝试连接
                try {
                    Thread.sleep(100); // 每次尝试间隔100毫秒
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return connection;
    }

    @Override
    public boolean testConnect() throws ClassNotFoundException {
        Class.forName(DATA_SOURCE_DRIVER_MYSQL);
        try {
            Connection conn = DriverManager.getConnection(url, username, password);//用参数得到连接对象
            if (conn.isValid(2000)){
                DataSourceCache.getInstance().putConnection(getHashCode(), conn);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Callable<Object> dataAcquisition() {
        return () -> {
            DataSourceCache cache = DataSourceCache.getInstance();
            Connection connection = (Connection) cache.getConnection(this);
            if (!connection.isValid(2000)) {
                connection = getConnection(DATA_SOURCE_CONNECT_TIMEOUT);
                DataSourceCache.getInstance().putConnection(getHashCode(), connection);
            }
            Map<String, List<T>> map = DatabaseAcquisitionUtil.acquisitionToMap(connection, dbName, dbTable);
            return map;
        };
    }

    @Override
    public Integer getHashCode() {
        return Objects.hash(url, username, password, dbName, dbTable);
    }
}
