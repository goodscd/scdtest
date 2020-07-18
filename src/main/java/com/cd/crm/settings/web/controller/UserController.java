package com.cd.crm.settings.web.controller;

import com.cd.crm.settings.domian.User;
import com.cd.crm.settings.service.UserService;
import com.cd.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理/settings/user/*.do请求的controller(处理器）类
 */

@Controller
@RequestMapping(value = "/settings/user")
public class UserController {
    //userService为代理类对象
    @Resource
    private UserService userService;
    //处理ajax请求
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String,Object> login(HttpServletRequest request,String loginAct, String loginPwd){
        Map<String,Object>  map=new HashMap<>();
        //将loginPwd密码的明文形式转化为MD5的密文形式，用于在数据库中做登录
        loginPwd= MD5Util.getMD5(loginPwd);
        //接收浏览p-[0器的ip地址:获取发起请求的浏览器的ip地址，用于      4.验证ip地址是否合法
        String ip=request.getRemoteAddr();
        System.out.println("ip地址："+ip);
        try {
            User user = userService.login(ip, loginAct, loginPwd);
            System.out.println("userService"+userService);
            //若无异常，则可以正常执行到这一步，即结果正确，将登录信息保存到session会话作用域之中
            request.getSession().setAttribute("user",user);
            map.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            String msg=e.getMessage();
            map.put("msg",msg);
        }
        return  map;
    }
}
