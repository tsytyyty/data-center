package com.data.center.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.center.factory.DataSource;
import com.data.center.factory.DataSourceFactory;
import com.data.center.mapper.DataSourceMapper;
import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.service.DataSourceService;
import com.data.center.utils.DataSourceCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceDo> implements DataSourceService {

    /**
     * 测试数据源，并加载到缓存中
     * 成功：200   文件不存在：404    测试失败：500
     */
    public int testDataSource(DataSourceDo dataSourceDo) throws Exception {
        return DataSourceFactory.build(dataSourceDo).testConnect();
    }

    /**
     * 测试数据源，添加之前
     */
    public Map<String, Object> testDataSourceBeforeAdd(DataSourceDo dataSourceDo) throws Exception {
        return DataSourceFactory.build(dataSourceDo).testConnectBeforeAdd();
    }

    /**
     * 新增数据源
     * 成功：200   文件不存在：404    测试失败：500
     */
    public Map<String, Object> addDataSource(DataSourceDo dataSourceDo) throws Exception {
        Map<String, Object> map = DataSourceFactory.build(dataSourceDo).testConnectBeforeAdd();
        if (map.get("code").equals(200)){
            dataSourceDo.setId(UUID.randomUUID().toString().replace("-",""));
            this.save(dataSourceDo);
            DataSourceCache.getInstance().putConnection(dataSourceDo.getId(),map.get("connection"));
            map.put("dataSourceDo",dataSourceDo);
            return map;
        }
        return map;
    }

    /**
     * 修改数据源，并更新缓存
     * 成功：200    数据源id不存在：403    文件不存在：404    测试失败：500
     */
    public Map<String, Object> updateDataSource(DataSourceDo dataSourceDo) throws Exception {
        //修改数据库，并更新缓存
        Map<String, Object> map = DataSourceFactory.build(dataSourceDo).testConnectBeforeAdd();
        if (map.get("code").equals(200)){
            boolean b = this.updateById(dataSourceDo);
            if (b){
                //更新缓存
                DataSourceCache.getInstance().putConnection(dataSourceDo.getId(),map.get("connection"));
                return map;
            }
            map.put("code",403);
        }
        return map;
    }

    /**
     * 删除数据源
     * 成功：200   数据源id不存在：403
     */
    public int deleteDataSource(String id) {
        DataSourceCache.getInstance().removeConnection(id);
        boolean b = this.removeById(id);
        return b ? 200 : 403;
    }

    /**
     * 查询所有数据源
     */
    public List<DataSourceDo> getAllDataSource() {
        QueryWrapper<DataSourceDo> qw = new QueryWrapper<>();
        qw.orderByAsc("type");
        return this.list(qw);
    }

}
