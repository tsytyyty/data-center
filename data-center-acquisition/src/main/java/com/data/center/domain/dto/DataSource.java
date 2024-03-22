package com.data.center.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public interface DataSource {

    /**
     * 获取连接
     */
    Object getConnection(long timeout) throws ClassNotFoundException, SQLException, InterruptedException;

    /**
     * 测试连接
     * @return  200成功   500失败
     */
    boolean testConnect() throws ClassNotFoundException;

    /**
     * 数据采集
     * @return  返回Runnable交给线程池执行
     */
    Callable<Object> dataAcquisition();

    /**
     * 获取数据源唯一标识
     */
    Integer getHashCode();

}
