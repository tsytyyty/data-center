package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 物流信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsInformation {

    //id
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    //提单号
    private String logisticsId;

    //货主名称
    private String owner;

    //货主代码（身份证号）
    private String ownerId;

    //物流公司（代码）
    private String companyId;

    //集装箱箱号
    private String containerId;

    //货物名称
    private String goodsName;

    //货重（吨）
    private int weight;

    //备注（错误信息）
    private String remark;

}
