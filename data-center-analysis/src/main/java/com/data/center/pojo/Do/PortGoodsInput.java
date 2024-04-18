package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("port_goods_input")
public class PortGoodsInput {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    //港口
    private String port;

    //货物
    private String goods;

    //入口量
    private int input;

    //占比
    private BigDecimal percentage;
}