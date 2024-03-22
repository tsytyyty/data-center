package com.data.center.domain.Do;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("customer_information")
public class CustomerInformation {      //客户信息

    //id
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    //姓名
    private String name;

    //身份证号
    private String idCard;

    //手机号
    private String phoneNumber;

    //地址（省市区）
    private String address;

    //备注（错误信息）
    private String remark;

}
