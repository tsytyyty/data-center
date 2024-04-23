package com.data.center.service.Impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.data.center.contant.CommunityConstant;
import com.data.center.mapper.UserMapper;
import com.data.center.pojo.Do.LoginTicket;
import com.data.center.pojo.Do.User;
import com.data.center.service.UserService;
import com.data.center.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;



    public Map<String, Object> login(String username, String password){

        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("errMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("errMsg", "密码不能为空!");
            return map;
        }

        //验证账号
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            map.put("errMsg", "该账号不存在!");
            return map;
        }
        //验证状态
        if (user.getActive() == 0) {
            map.put("errMsg", "该账号未激活!");
            return map;
        }

        //验证密码
//        password = UUID.md5(password);
        if (!user.getPassword().equals(password)) {
            map.put("errMsg", "密码不正确!");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(UUID.getUUID());
        loginTicket.setPower(user.getActive());

        //废除之前登录凭证
        userMapper.updateTicketStatus(user.getId());
        //存入新登录凭证
        userMapper.insertTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        map.put("power",user.getActive() == 1 ? AUTHORITY_USER : AUTHORITY_ADMIN);
        return map;
    }


    /**
     * 注册
     */
    public Map<String, Object> register(String username, String password, String realName, String idCard,
                                        String phone, String email){

        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (username.isEmpty()){
            map.put("errMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("errMsg", "密码不能为空!");
            return map;
        }
        if (realName.isEmpty()){
            map.put("errMsg", "真实姓名不能为空！");
            return map;
        }
        if (idCard.isEmpty()){
            map.put("errMsg", "工号不能为空！");
            return map;
        }
        if (phone.isEmpty()){
            map.put("errMsg", "手机号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(email)) {
            map.put("errMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectUserByUsername(username);
        if (u != null) {
            map.put("errorMsg", "该账号已存在!");
            return map;
        }
        u = userMapper.selectUserByIdCard(idCard);
        if (u != null) {
            map.put("errorMsg", "该工号已被注册!");
            return map;
        }

        //注册用户
        User user = new User();
        user.setUsername(username);
//        user.setPassword(UUID.md5(password));
        user.setPassword(password);
        user.setEmail(email);
        user.setActive(0);
        user.setPhone(phone);
        user.setRealName(realName);
        user.setIdCard(idCard);

        userMapper.insertUser(user);

        return map;
    }

//    public Collection<? extends GrantedAuthority> getAuthorities(int power) {
//
//        List<GrantedAuthority> list = new ArrayList<>();
//        list.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//                switch (power) {
//                    case 2:
//                        return AUTHORITY_ADMIN;
//                    default:
//                        return AUTHORITY_USER;
//                }
//            }
//        });
//        return list;
//    }

    /**
     * 根据ticket查找LoginTicket
     */
    public LoginTicket findLoginTicket(String ticket){
        return userMapper.selectTicketByTicket(ticket);
    }

    /**
     * 根据id查找用户
     */
    public User findUserById(int id){
        return userMapper.selectUserById(id);
    }

    /**
     * 用户授权
     */
    public int updatePower(int id, int power){
        return userMapper.updatePower(id, power);
    }

    /**
     * 需要授权用户列表
     */
    public List<User> needPowerUser(){
        return userMapper.needPowerUser();
    }

    /**
     * 查询所有用户
     */
    public List<User> allUser(){
        return userMapper.selectAllUser();
    }



}
