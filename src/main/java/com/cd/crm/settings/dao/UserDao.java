package com.cd.crm.settings.dao;

import com.cd.crm.settings.domian.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    //用户登录操作所涉及的sql语句
    User login(Map<String,String> map);
    //在workbench中的模态窗口中利用ajax请求完成下拉列表所有者的sql语句
    List<User> getUserList();
}
