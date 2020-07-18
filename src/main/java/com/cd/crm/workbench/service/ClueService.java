package com.cd.crm.workbench.service;

import com.cd.crm.exception.ClueException;
import com.cd.crm.settings.domian.User;
import com.cd.crm.workbench.domian.Activity;
import com.cd.crm.workbench.domian.Clue;
import com.cd.crm.workbench.domian.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<User> getUserList();

    boolean saveClue(Clue clue) throws ClueException;

    Clue detail(String id);

    List<Activity> getActivityListClueId(String clueId);

    boolean delClueActivityRelation(String id) throws ClueException;

    List<Activity> getActivityByList(Map<String, String> map);

    boolean getClueActivityRelation(Map<String, Object> map) throws ClueException;

    List<Activity> getActivityAndClueId(String aname, String clueId);

    boolean convert(String clueId, Tran tran, String createBy) throws ClueException;
}
