package com.cd.crm.workbench.dao;


import com.cd.crm.workbench.domian.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {
    int delClueActivityRelation(String id);

    int getClueActivityRelation(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> getActivityRelationList(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
