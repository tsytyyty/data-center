package com.data.center.mapper;


import com.data.center.pojo.Do.LoginTicket;
import com.data.center.pojo.Do.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据id查询用户
     */
    User selectUserById(int id);

    /**
     * 根据username查询用户
     */
    User selectUserByUsername(String username);

    /**
     * 根据idCard查询用户
     */
    User selectUserByIdCard(String idCard);

    /**
     * 添加用户
     */
    int insertUser(User user);

    /**
     * 根据ticket查询LoginTicket
     */
    LoginTicket selectTicketByTicket(String ticket);

    /**
     * 保存用户LoginTicket
     */
    int insertTicket(LoginTicket loginTicket);

    /**
     * ticket过期状态
     */
    int updateTicketStatus(int userId);

    /**
     * 用户授权
     */
    int updatePower(int id, int power);

    /**
     * 查询需要授权用户
     */
    List<User> needPowerUser();

    /**
     * 查寻所有用户
     */
    List<User> selectAllUser();

}
