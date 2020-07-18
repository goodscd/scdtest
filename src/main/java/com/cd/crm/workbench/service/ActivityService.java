package com.cd.crm.workbench.service;

import com.cd.crm.exception.ActivityException;
import com.cd.crm.vo.PaginationVo;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity) throws ActivityException;
    PaginationVo<Activity> pageList(Map<String,Object> map);
    boolean delete(String[] ids) throws ActivityException;

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity) throws ActivityException;

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String id);

    boolean deletRemark(String id) throws ActivityException;

    boolean saveRemark(ActivityRemark remark) throws ActivityException;
}
