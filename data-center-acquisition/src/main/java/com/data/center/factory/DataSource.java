package com.data.center.factory;

import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface DataSource {

    /**
     * 获取连接
     */
    Object getConnection(long timeout) throws ClassNotFoundException, SQLException, InterruptedException;

    /**
     * 测试连接，添加之前
     * @return  200成功   404未找到文件   500连接失败
     */
    Map<String, Object> testConnectBeforeAdd() throws Exception;

    /**
     * 200成功   500连接失败
     * 测试已有数据源
     */
    int testConnect() throws Exception;

    /**
     * 数据采集
     * @return  返回Runnable交给线程池执行
     */
    Callable<Map<String, List<Object>>> dataAcquisition();

    /**
     * 获取数据源的UUID
     */
    String getUUID();

}
