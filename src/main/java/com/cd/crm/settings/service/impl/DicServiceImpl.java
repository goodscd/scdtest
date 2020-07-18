package com.cd.crm.settings.service.impl;

import com.cd.crm.settings.dao.DicTypeDao;
import com.cd.crm.settings.dao.DicValueDao;
import com.cd.crm.settings.domian.DicType;
import com.cd.crm.settings.domian.DicValue;
import com.cd.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "dicService")
public class DicServiceImpl implements DicService {
    @Autowired
    @Qualifier(value = "dicTypeDao")
    private DicTypeDao dicTypeDao;
    @Resource(name = "dicValueDao")
    private DicValueDao dicValueDao;
    @Override
    public Map<String, List<DicValue>> getDictionaries(){
        Map<String,List<DicValue>> map=new HashMap<>();
        List<DicType> dicTypes = dicTypeDao.selectDicType();
        for (DicType dicType : dicTypes) {
            String typeCode=dicType.getCode();
            List<DicValue> dicValues=dicValueDao.selectDicValue(typeCode);
            map.put(typeCode,dicValues);
        }
        return map;
    }

}
