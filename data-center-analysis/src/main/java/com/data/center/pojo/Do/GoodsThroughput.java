package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_throughput")
public class GoodsThroughput {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    // 货物名称
    private String goods;

    // 货物吞吐量
    private int throughput;

    // 货物吞吐量百分比
    private BigDecimal percentage;
}
