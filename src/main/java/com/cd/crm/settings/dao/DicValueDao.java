package com.cd.crm.settings.dao;

import com.cd.crm.settings.domian.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> selectDicValue(String typeCode);
}
