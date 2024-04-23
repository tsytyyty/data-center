package com.data.center.controller;


import com.alibaba.fastjson.JSON;
import com.data.center.pojo.result.Result;
import com.data.center.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@ResponseBody
public class LoginController {

    @Autowired
    private UserService userService;


    /**
     * 注册
     */
    @PostMapping("/register")
    public String register(String username, String password, String realName, String idCard,
                           String phone, String email){

        Map<String, Object> map = userService.register(username, password, realName, idCard, phone, email);
        if (map == null || map.isEmpty()) {
            return JSON.toJSONString(new Result(0, "注册成功，请等待管理员确认授权！", map));
        }else {
            return JSON.toJSONString(new Result(404, "注册失败！", map));
        }

    }


    /**
     * 登录
     */
    @PostMapping("/login")
    public String login(String username, String password, HttpServletResponse response){
        //检验账号密码
        Map<String, Object> map = userService.login(username, password);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
            response.addCookie(cookie);
            return JSON.toJSONString(new Result(0, "登录成功！", map));
        } else {
            return JSON.toJSONString(new Result(404, "登录失败！", map));
        }
    }





}
