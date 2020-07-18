package com.cd.crm.settings.login;

import com.cd.crm.settings.dao.DicTypeDao;
import com.cd.crm.settings.dao.DicValueDao;
import com.cd.crm.settings.dao.UserDao;
import com.cd.crm.settings.domian.DicType;
import com.cd.crm.settings.domian.DicValue;
import com.cd.crm.settings.domian.User;
import com.cd.crm.settings.service.DicService;
import com.cd.crm.settings.service.UserService;
import com.cd.crm.utils.DateTimeUtil;
import com.cd.crm.utils.MD5Util;
import com.cd.crm.workbench.dao.ActivityDao;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.service.ActivityService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录操作后台验证
 */

public class test {
    public static void main(String[] args) {
        /**
         * 1.验证账号密码是否正确（密码采用dm5加密格式）
         * 2.验证账号是否失效
         * 3.验证账号是否被锁定
         * 4.验证浏览器ip地址是否可用
         */
        //2.验证失效时间
        String exprieTime="2019-10-10 10:10:10";
        Date date=new Date();
        //将时间对象转化为时间字符串
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currnetTime= simpleDateFormat.format(date);
        System.out.println(currnetTime);
        //验证时间是否失效:使用字符串比较方法，若返回为正数说明未失效，若返回负数，说明密码已失效
        int i = exprieTime.compareTo(currnetTime);
        System.out.println(i);


        //使用时间工具类:获取当前系统时间
        String sysTime = DateTimeUtil.getSysTime();
        System.out.println(sysTime);
        int i1 = exprieTime.compareTo(sysTime);
        System.out.println(i1);



    //3.验证账号是否被锁定：lockState为0，则说明账号已锁定，无法使用
        String lockState="0";
        if ("0".equals(lockState)) {
            System.out.println("账号已锁定");
        }



    //4.验证ip地址是否合法
       String ip="192.168.1.1";
    String allowIps="192.168.1.1,192.168.1.2";
    //contains:字符串方法，是否包含
        if (allowIps.contains(ip)) {
            System.out.println("有效的ip地址，可以访问系统");
        }else {
            System.out.println("无效的ip地址，无法访问系统");
        }



    //5.密码解析，MD5密码转化，将用户发送的请求参数密码转化为MD5密文，用于验证登录
        String pwd="wangpaidoubao";
        pwd= MD5Util.getMD5(pwd);
        System.out.println(pwd);

    }
    @Test
    public  void test(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        ActivityDao activityDao = (ActivityDao) ac.getBean("activityDao");
        System.out.println("activityDao的对象为："+activityDao);
    }
    @Test
    public  void test1(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao dao = (UserDao) ac.getBean("userDao");
        System.out.println("userDao的对象为："+dao);
        Map<String,String> map=new  HashMap<>();
        map.put("loginAct","zs");
        map.put("loginPwd","123");
        User user = dao.login(map);
        System.out.println(user);
    }
    @Test
    public  void test2(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) ac.getBean("userService");
        System.out.println("userService的对象为："+userService);
    }
    @Test
    public  void test3(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        ActivityService activityService = (ActivityService) ac.getBean("service");
        System.out.println("service的对象为："+ activityService);
    }
    @Test
    public  void tes4(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        ActivityDao activityDao = (ActivityDao) ac.getBean("activityDao");
        Activity detail = activityDao.detail("ed58a5d9d75c49afb9f8c3f9e4735fae");
        System.out.println(detail);
    }
    @Test
    public  void tes5(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        DicService dicService = (DicService) ac.getBean("dicService");
        Map<String, List<DicValue>> dictionaries = dicService.getDictionaries();
        System.out.println(dictionaries);
    }
    @Test
    public  void tes6(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        DicTypeDao dicTypeDao = (DicTypeDao) ac.getBean("dicTypeDao");
        List<DicType> dicTypes = dicTypeDao.selectDicType();
        System.out.println(dicTypes);
    }
    @Test
    public  void tes06(){
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        DicValueDao dicValueDao = (DicValueDao) ac.getBean("dicValueDao");
        List<DicValue> dicValues = dicValueDao.selectDicValue("source");
        System.out.println(dicValues);
    }







}
