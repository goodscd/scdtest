package com.cd.crm.workbench.dao;

import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int insertActivity(Activity activity);
    List<Activity> getActivityListByCondition(Map<String, Object> map);
    int getTotalByCondition(Map<String, Object> map);
    int delete(String[] ids);

    Activity getActivity(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListClueId(String clueId);

    List<Activity> getActivityByList(Map<String, String> map);

    List<Activity> getActivityAndClueId(@Param("aname") String aname, @Param("clueId") String clueId);
}
