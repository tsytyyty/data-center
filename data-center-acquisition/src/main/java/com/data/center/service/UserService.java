package com.data.center.service;

import com.data.center.pojo.Do.LoginTicket;
import com.data.center.pojo.Do.User;

import java.util.List;
import java.util.Map;

public interface UserService{

    Map<String, Object> login(String username, String password);

    Map<String, Object> register(String username, String password, String realName, String idCard,
                                 String phone, String email);

    /**
     * 根据ticket查找LoginTicket
     */
    public LoginTicket findLoginTicket(String ticket);

    /**
     * 根据id查找用户
     */
    public User findUserById(int id);

    /**
     * 用户授权
     */
    public int updatePower(int id, int power);

    /**
     * 需要授权用户列表
     */
    public List<User> needPowerUser();

    /**
     * 查询所有用户
     */
    public List<User> allUser();

}
