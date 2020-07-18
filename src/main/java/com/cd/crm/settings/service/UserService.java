package com.cd.crm.settings.service;

import com.cd.crm.exception.LoginException;
import com.cd.crm.settings.domian.User;

import java.util.List;

public interface UserService {
    User login(String ip,String loginAct, String loginPwd) throws LoginException;
    List<User> getUserList();
}
