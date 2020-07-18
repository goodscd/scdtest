package com.cd.crm.workbench.web.controller;

import com.cd.crm.exception.ActivityException;
import com.cd.crm.settings.domian.User;
import com.cd.crm.settings.service.UserService;
import com.cd.crm.utils.DateTimeUtil;
import com.cd.crm.utils.UUIDUtil;
import com.cd.crm.vo.PaginationVo;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.ActivityRemark;
import com.cd.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "/workbench/activity")
public class ActivityController {
    @Autowired
    @Qualifier(value = "activityService")
    private ActivityService activityService;
    @Resource(name = "userService")
    private UserService userService;
    //接收ajax请求，在市场活动中打开创建页面之前，将所有者的姓名部署上去
    @RequestMapping(value = "/getUserList.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return  userList;
    }
    //接收ajax请求，完成activity表中数据的添加
    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<Object,Object> save(HttpServletRequest request, Activity activity){//使用对象接收参数
        Map map=new HashMap();
        //用md5编译id
        String id= UUIDUtil.getUUID();
        //获取当前时间
        String createTime= DateTimeUtil.getSysTime();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //获取创建人
        String createBy=user.getName();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        boolean flag = false;
        try {
            flag = activityService.save(activity);
            System.out.println(flag);
            map.put("success",flag);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",flag);
        }
        return  map;
    }
    @RequestMapping(value = "/pageList.do",method = RequestMethod.GET)
    @ResponseBody
    //返回的结果为vo类数据，即   {total:总记录条数，dataList：[json数组]}
    public PaginationVo<Activity> pageList(String pageNo, String pageSize, String name, String owner, String startDate, String endDate ){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNo",Integer.valueOf(pageNo));
        map.put("pageSize",Integer.valueOf(pageSize));
        System.out.println("owner:"+owner);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        PaginationVo<Activity> vo = activityService.pageList(map);
        return  vo;
    }
    @RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<Object,Object> delete(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        Map<Object, Object> map = new HashMap<>();
        boolean flag=true;
        try {
            flag= activityService.delete(ids);
            map.put("success",flag);
        } catch (ActivityException e) {
            e.printStackTrace();
            map.put("success",flag);
            String msg = e.getMessage();
            map.put("msg",msg);
        }
        return  map;
    }
    @RequestMapping(value = "/getUserListAndActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        Map<String,Object> map=new HashMap<>();
        map=activityService.getUserListAndActivity(id);
        return  map;
    }
    //修改操作
    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> update(HttpServletRequest request,Activity activity){
        String editTime=DateTimeUtil.getSysTime();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String editBy=user.getName();
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        boolean flag= true;
        Map<String,Boolean> map=new HashMap<>();
        try {
            flag = activityService.update(activity);
            map.put("success",flag);
        } catch (ActivityException e) {
            e.printStackTrace();
            map.put("success",flag);
        }
        return map;
    }
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ModelAndView detail(HttpServletRequest request,String id){
        ModelAndView mv=new ModelAndView();
        Activity activity=activityService.detail(id);
        mv.addObject("activity",activity);
        mv.setViewName("forward:/workbench/activity/detail.jsp");
        return mv;
    }
    //获取备注列表
    @RequestMapping(value = "getRemarkListByAid.do",method = RequestMethod.GET)
    @ResponseBody
    public  List<ActivityRemark>  getRemarkListByAid(@RequestParam(value = "activityid") String id){
        List<ActivityRemark> list=activityService.getRemarkListByAid(id);
        return list;

    }
    //删除备注
    @RequestMapping(value = "deleteRemark.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> deleteRemark(String id){
        Map<String,Boolean> map=new HashMap<>();
        boolean flag=true;
        try {
            flag=activityService.deletRemark(id);
            map.put("success",flag);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",flag);
        }
        return map;
    }
    //添加备注
    @RequestMapping(value = "/saveRemark.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveRemark(HttpServletRequest request,ActivityRemark remark){
        String createTime=DateTimeUtil.getSysTime();
        String id=UUIDUtil.getUUID();
        String editflag="0";
        User user=(User)(request.getSession().getAttribute("user"));
        String createBy=user.getName();
        remark.setCreateTime(createTime);
        remark.setCreateBy(createBy);
        remark.setEditFlag(editflag);
        remark.setId(id);
        boolean  flag=true;
        Map<String,Object> map=new HashMap<>();
        try{
            flag=activityService.saveRemark(remark);
            map.put("success",flag);
            map.put("remark",remark);
        }catch (Exception e){
            map.put("success",flag);
        }
       return map;
    }

}
