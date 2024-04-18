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
@TableName("port_goods_flow_time")
public class PortGoodsFlowTime {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    //港口
    private String port;

    //货物
    private String goods;

    //平均流转时间
    private BigDecimal flowTime;
}
