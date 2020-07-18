package com.cd.crm.settings.service.impl;

import com.cd.crm.exception.LoginException;
import com.cd.crm.settings.dao.UserDao;
import com.cd.crm.settings.domian.User;
import com.cd.crm.settings.service.UserService;
import com.cd.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier(value = "userDao")
    private UserDao userDao;
    @Transactional
    @Override
    public User login(String ip,String loginAct, String loginPwd) throws LoginException {
        Map<String,String> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);
        //1.用户名密码，错误
        if (user==null) {
            throw new LoginException("用户名密码错误");
        }
        //2.账号已失效
        String expireTime=user.getExpireTime();//账号失效时间
        String sysTime= DateTimeUtil.getSysTime();//当前系统时间
        if (expireTime.compareTo(sysTime)<0) {
            throw  new LoginException("该账号已失效");
        }
        //3.ip地址不符合
        String allowIps = user.getAllowIps();//发起请求的浏览器的ip地址
        if (!allowIps.contains(ip)) {
            throw  new LoginException("ip地址受限");
        }
        //4.判断账号是否被锁定
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw  new LoginException("该账号被冻结，请联系管理员");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
