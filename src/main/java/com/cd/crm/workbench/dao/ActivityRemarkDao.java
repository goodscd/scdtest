package com.cd.crm.workbench.dao;

import com.cd.crm.workbench.domian.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDao {
    //计算要删除的数量
    int ConuntDeleteActivityRemark(String id[]);
    //进行删除操作
    int DeleteActivityRemark(String[] ids);

    List<ActivityRemark> getRemarkListByAid(@Param(value = "activity") String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark remark);
}
