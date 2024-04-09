package com.data.center.pojo.Do;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnloadingTable {       //卸货表

    //id
    private long id;

    //船公司
    private String shipCompanies;

    //船名称
    private String shipName;

    //作业开始时间
    private Date workBeginTime;

    //作业结束时间
    private Date workEndTime;

    //始发时间
    private Date departureTime;

    //到达时间
    private Date arriveTime;

    //作业港口
    private String port;

    //提单号
    private String logisticsId;

    //集装箱箱号
    private String containerId;

    //箱尺寸（TEU）
    private int containerSize;

    //启运地
    private String departurePlace;

    //目的地
    private String destinationPlace;

    //备注（错误信息）
    @TableField(exist = false)
    private String remark;

}
