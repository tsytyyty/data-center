package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("port_yoy_qoq")
public class PortYoyQoq {

    @TableId(value = "id", type = IdType.AUTO)
    int id;

    //港口名称
    private String port;

    //年份
    private int year;

    //月份
    private int month;

    //吞吐量
    private int throughput;

    //同比
    private BigDecimal yoy;

    //环比
    private BigDecimal qoq;

}
