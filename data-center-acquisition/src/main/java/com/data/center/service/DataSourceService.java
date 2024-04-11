package com.data.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.center.pojo.Do.DataSourceDo;

import java.util.List;
import java.util.Map;

public interface DataSourceService extends IService<DataSourceDo> {

    /**
     * 测试数据源，并加载到缓存中
     * 成功：200   文件不存在：404    测试失败：500
     */
    int testDataSource(DataSourceDo dataSourceDo) throws Exception;

    /**
     * 测试数据源，添加之前
     */
    Map<String, Object> testDataSourceBeforeAdd(DataSourceDo dataSourceDo) throws Exception;

    /**
     * 新增数据源
     * 成功：200   文件不存在：404    测试失败：500
     */
    Map<String, Object> addDataSource(DataSourceDo dataSourceDo) throws Exception;

    /**
     * 修改数据源，并更新缓存
     * 成功：200    数据源id不存在：403    文件不存在：404    测试失败：500
     */
    Map<String, Object> updateDataSource(DataSourceDo dataSourceDo) throws Exception ;

    /**
     * 删除数据源
     * 成功：200   数据源id不存在：403
     */
    int deleteDataSource(String id);

    /**
     * 查询所有数据源
     */
    List<DataSourceDo> getAllDataSource();

}
