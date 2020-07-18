package com.cd.crm.workbench.service.impl;

import com.cd.crm.exception.ActivityException;
import com.cd.crm.settings.dao.UserDao;
import com.cd.crm.settings.domian.User;
import com.cd.crm.settings.service.UserService;
import com.cd.crm.vo.PaginationVo;
import com.cd.crm.workbench.dao.ActivityDao;
import com.cd.crm.workbench.dao.ActivityRemarkDao;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.ActivityRemark;
import com.cd.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "activityService")
public class ActivityServiceImpl implements ActivityService {
    @Resource(name = "activityDao")
    private ActivityDao activityDao;
    @Autowired
    @Qualifier(value = "activityRemarkDao")
    private ActivityRemarkDao activityRemarkDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Override
    public boolean save(Activity activity) throws ActivityException {
        int i = activityDao.insertActivity(activity);
        if (i==1) {
            return  true;
        }
        if(i!=1){
            throw new ActivityException();
        }
        return false;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        Integer pageNo=(Integer) map.get("pageNo");
        Integer pageSize=(Integer)map.get("pageSize");
        //执行分页操作
        PageHelper.startPage(pageNo,pageSize);
        List<Activity> activities = activityDao.getActivityListByCondition(map);
        int total=activityDao.getTotalByCondition(map);
        PaginationVo<Activity> vo=new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(activities);
        return vo;
    }
    @Transactional
    @Override
    public boolean delete(String[] ids) throws ActivityException {
        boolean flag=true;
        int count1 = activityRemarkDao.ConuntDeleteActivityRemark(ids);
        int count2 = activityRemarkDao.DeleteActivityRemark(ids);
        if (count1!=count2){
            flag=false;
            throw  new ActivityException("删除失败");
        }
        int count3 = activityDao.delete(ids);
        if (count3!=ids.length) {
            flag=false;
            throw  new ActivityException("删除失败");
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String,Object> map=new HashMap<>();
        List<User> uList = userDao.getUserList();
        Activity activity=activityDao.getActivity(id);
        map.put("uList",uList);
        map.put("activity",activity);
        return map;
    }
    //修改操作

    @Override
    public boolean update(Activity activity) throws ActivityException {
        boolean flag=false;
        int i=activityDao.update(activity);
        if (i==1) {
            flag=true;
        }else{
            flag=false;
            throw new ActivityException("修改信息失败");
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity=activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String id) {
        List<ActivityRemark> list=activityRemarkDao.getRemarkListByAid(id);
        return list;
    }

    @Override
    public boolean deletRemark(String id) throws ActivityException {
        int i=activityRemarkDao.deleteRemark(id);
        boolean flag=true;
        if (i==1) {
            flag=true;
        }else{
            flag=false;
            throw new ActivityException("删除失败");
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark remark) throws ActivityException {
        int i=activityRemarkDao.saveRemark(remark);
        boolean flag=true;
        if (i==1) {
            flag=true;
        }else{
            flag=false;
            throw  new ActivityException("添加失败");
        }
        return flag;
    }
}
