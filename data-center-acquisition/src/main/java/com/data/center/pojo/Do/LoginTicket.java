package com.data.center.pojo.Do;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    // 0：未激活 1：普通用户 2：管理员
    private int power;      //权限
    // 0：可用 1：已过期
    private int status;     //状态

}
