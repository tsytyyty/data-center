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

        dataSourceDo.setId(dataSourceDto.getId());
        dataSourceDo.setName(dataSourceDto.getName());
        dataSourceDo.setUrl(dataSourceDto.getUrl());
        dataSourceDo.setUsername(dataSourceDto.getUsername());
        dataSourceDo.setPassword(dataSourceDto.getPassword());
        dataSourceDo.setBucketName(dataSourceDto.getBucketName());
        dataSourceDo.setDbName(dataSourceDto.getDbName());
        dataSourceDo.setType(dataSourceDto.getType());
        Map<String, String> dataAndType = new HashMap<>();
        dataSourceDto.getDataAndType().forEach(s -> {
            String[] split = s.split("!");
            dataAndType.put(split[0], split[1]);
        });
        dataSourceDo.setDataAndType(dataAndType);
        return dataSourceDo;
    }

    //do转dto
    public static DataSourceDto DataSourceDoToDto(DataSourceDo dataSourceDo) {
        DataSourceDto dataSourceDto = new DataSourceDto();

        dataSourceDto.setId(dataSourceDo.getId());
        dataSourceDto.setName(dataSourceDo.getName());
        dataSourceDto.setUrl(dataSourceDo.getUrl());
        dataSourceDto.setUsername(dataSourceDo.getUsername());
        dataSourceDto.setPassword(dataSourceDo.getPassword());
        dataSourceDto.setBucketName(dataSourceDo.getBucketName());
        dataSourceDto.setDbName(dataSourceDo.getDbName());
        dataSourceDto.setType(dataSourceDo.getType());


        List<String> list = new ArrayList<>();
        dataSourceDo.getDataAndType().forEach((k, v) -> {
            list.add(k + "!" + v);
        });
        dataSourceDto.setDataAndType(list);
        return dataSourceDto;
    }

}
