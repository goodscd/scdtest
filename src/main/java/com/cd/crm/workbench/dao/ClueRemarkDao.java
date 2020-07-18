package com.cd.crm.workbench.dao;

import com.cd.crm.workbench.domian.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
