package com.data.center.pojo.Do;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;             //id
    private String username;    //用户名
    private String password;    //密码
    private String realName;    //真实姓名
    private String idCard;      //工号
    private String phone;       //手机号
    private String email;       //邮箱
    private int active;         //授权状态 0：未授权 1：普通用户 2：管理员

}
