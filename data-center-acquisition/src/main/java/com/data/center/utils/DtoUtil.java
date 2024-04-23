package com.data.center.utils;

import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.pojo.dto.DataSourceDto;

import java.util.HashMap;
import java.util.Map;

public class DtoUtil {

    public static DataSourceDo DataSourceDtoToDo(DataSourceDto dataSourceDto) {
        DataSourceDo dataSourceDo = new DataSourceDo();
        dataSourceDo.setName(dataSourceDto.getName());
        dataSourceDo.setUrl(dataSourceDto.getUrl());
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

}
