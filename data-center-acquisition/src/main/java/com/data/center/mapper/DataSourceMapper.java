package com.data.center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.center.pojo.Do.DataSourceDo;
import org.apache.ibatis.annotations.Mapper;

import javax.sql.DataSource;

@Mapper
public interface DataSourceMapper extends BaseMapper<DataSourceDo> {
}
