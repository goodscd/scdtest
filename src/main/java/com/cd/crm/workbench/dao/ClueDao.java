package com.cd.crm.workbench.dao;


import com.cd.crm.workbench.domian.Clue;

public interface ClueDao {


    int saveClue(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
