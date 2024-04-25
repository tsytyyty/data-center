package com.data.center.utils;

import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.pojo.dto.DataSourceDto;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoUtil {

    public static DataSourceDo DataSourceDtoToDo(DataSourceDto dataSourceDto) {
        DataSourceDo dataSourceDo = new DataSourceDo();
        dataSourceDo.setName(dataSourceDto.getName());
        dataSourceDo.setUrl(dataSourceDto.getUrl());
        dataSourceDo.setPassword(dataSourceDto.getPassword());
        Map<String, String> dataAndType = new HashMap<>();
        dataSourceDto.getDataAndType().forEach(s -> {
            String[] split = s.split("_");
            dataAndType.put(split[0], split[1]);
        });
        dataSourceDo.setDataAndType(dataAndType);
        dataSourceDo.setType(dataSourceDto.getType());
        dataSourceDo.setId(dataSourceDto.getId());
        dataSourceDo.setUsername(dataSourceDto.getUsername());
        return dataSourceDo;
    }

    //do转dto
    public static DataSourceDto DataSourceDoToDto(DataSourceDo dataSourceDo) {
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setName(dataSourceDo.getName());
        dataSourceDto.setUrl(dataSourceDo.getUrl());
        dataSourceDto.setType(dataSourceDo.getType());
        dataSourceDto.setId(dataSourceDo.getId());
        dataSourceDto.setUsername(dataSourceDo.getUsername());
        dataSourceDto.setPassword(dataSourceDo.getPassword());
        dataSourceDto.setDbName(dataSourceDo.getDbName());
        dataSourceDto.setBucketName(dataSourceDo.getBucketName());
        List<String> list = new ArrayList<>();
        dataSourceDo.getDataAndType().forEach((k, v) -> {
            list.add(k + "_" + v);
        });
        dataSourceDto.setDataAndType(list);
        return dataSourceDto;
    }

}
