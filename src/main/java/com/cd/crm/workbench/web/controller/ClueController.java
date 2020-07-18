package com.cd.crm.workbench.web.controller;

import com.cd.crm.exception.ClueException;
import com.cd.crm.settings.domian.User;
import com.cd.crm.utils.DateTimeUtil;
import com.cd.crm.utils.UUIDUtil;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.Clue;
import com.cd.crm.workbench.domian.Tran;
import com.cd.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {
    @Autowired
    @Qualifier(value = "clueService")
    private ClueService clueService;
    @RequestMapping(value = "/getUserList.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList=clueService.getUserList();
        return  userList;
    }
    @RequestMapping(value = "saveClue.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> saveClue(HttpServletRequest request,Clue clue){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String name = user.getName();
        clue.setCreateTime(createTime);
        clue.setId(id);
        clue.setCreateBy(name);
        boolean flag=true;
        Map<String,Boolean> map=new HashMap<>();
        try {
            flag = clueService.saveClue(clue);
            map.put("success",flag);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
        }
        return map;
    }
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ModelAndView detail(String id){
        ModelAndView mv=new ModelAndView();
        Clue clue=clueService.detail(id);
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;

    }
    @RequestMapping(value = "/getActivityListClueId.do",method = RequestMethod.GET)
    @ResponseBody
    public List<Activity> getActivityListClueId(String clueId){
        List<Activity> list=clueService.getActivityListClueId(clueId);
        return list;

    }
    @RequestMapping(value = "/delClueActivityRelation.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> delClueActivityRelation(String id){
        boolean flag=true;
        Map<String,Boolean> map=new HashMap<>();
        try {
            flag=clueService.delClueActivityRelation(id);
            map.put("success",flag);
        } catch (Exception e) {
            map.put("success",flag);
            e.printStackTrace();
        }
        return map;
    }
    @RequestMapping(value = "/getActivityByList.do",method = RequestMethod.GET)
    @ResponseBody
    public  List<Activity> getActivityByList(String aname,String clueId){
        Map<String,String> map=new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> list=clueService.getActivityByList(map);
        return list;

    }
    @RequestMapping(value = "/getClueActivityRelation.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Boolean> getClueActivityRelation(HttpServletRequest request){
        String[] activityIds = request.getParameterValues("activityId");
        System.out.println(activityIds);
        String clueId = request.getParameter("clueId");
        System.out.println(clueId);
        Map<String,Object> map=new HashMap<>();
        map.put("activityIds",activityIds);
        map.put("clueId",clueId);
        boolean flag=true;
        Map<String,Boolean> map1=new HashMap<>();
        try {
            flag=clueService.getClueActivityRelation(map);
            map1.put("success",flag);
        } catch (Exception e) {
            map1.put("success",false);
            e.printStackTrace();
        }
        return map1;

    }
    @RequestMapping(value = "/getActivityAndClueId.do",method = RequestMethod.GET)
    @ResponseBody
    public List<Activity> getActivityAndClueId(String aname,String clueId){
        List<Activity> activities=clueService.getActivityAndClueId(aname,clueId);
        return activities;
    }
    @RequestMapping(value = "/convert.do",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView convent(HttpServletRequest request,String clueId,@RequestParam(value = "flag",required = false) String flag){
        ModelAndView mv=new ModelAndView();
        Tran tran=null;
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)) {
            tran=new Tran();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id=UUIDUtil.getUUID();
            String createTime=DateTimeUtil.getSysTime();
            tran.setId(id);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
        }
        boolean flag1= false;
        try {
            flag1 = clueService.convert(clueId,tran,createBy);
            if (flag1){
                mv.setViewName("/workbench/clue/index.jsp");
            }
        } catch (ClueException e) {
            e.printStackTrace();
        }

        return mv;
    }
}
