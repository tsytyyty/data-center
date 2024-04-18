package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tabulate_data")
public class TabulateData {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    //港口
    private String port;

    //货物
    private String goods;

    //年份
    private int year;

    //月份
    private int month;

    //吞吐量
    private int throughput;
}
