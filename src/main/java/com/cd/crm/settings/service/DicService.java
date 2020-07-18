package com.cd.crm.settings.service;

import com.cd.crm.settings.dao.DicTypeDao;
import com.cd.crm.settings.dao.DicValueDao;
import com.cd.crm.settings.domian.DicType;
import com.cd.crm.settings.domian.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    public Map<String, List<DicValue>> getDictionaries();

}
