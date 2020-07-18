package com.cd.crm.settings.web.listener;

import com.cd.crm.settings.dao.DicTypeDao;
import com.cd.crm.settings.dao.DicValueDao;
import com.cd.crm.settings.domian.DicType;
import com.cd.crm.settings.domian.DicValue;
import com.cd.crm.settings.service.DicService;
import com.cd.crm.settings.service.impl.DicServiceImpl;
import com.cd.crm.utils.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
 /*   @Autowired
    @Qualifier(value = "disService")
    private DicService dicService;*/
    //在服务器启动的时候，在全局作用域对象中保存数据字典
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
/*        DicService dicService =(DicService)ServiceFactory.getService(new DicServiceImpl());
        ServletContext application = servletContextEvent.getServletContext();
        //数据字典中的数据存储格式为map<String,List<DicValue>>
        Map<String, List<DicValue>> map = dicService.getDictionaries();
        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<DicValue> dicValues = map.get(code);
            //将数据字典保存到全局作用域之中
            application.setAttribute(code,dicValues);
        }
        System.out.println("map的值为=========="+map);*/
        ApplicationContext applicationContext =WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        DicService dicService = applicationContext.getBean(DicService.class);
        ServletContext application = servletContextEvent.getServletContext();
        Map<String, List<DicValue>> map = dicService.getDictionaries();
        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<DicValue> dicValues = map.get(code);
            //将数据字典保存到全局作用域之中
            application.setAttribute(code,dicValues);
        }
        /*
        * 数据字典的作用：
        *     在下拉框刷新的时候，进行表单赋值
        * map<String,List<DicValue>>
                 String为DicKey中的主键即code,而Dickey表中的主键是DicValue表中的外键（typeCode)
                 ,List<DicValue>中则保存的是需要向select框中填写的文本，code为select的name属性
          数据字典的优点：在下拉框获取值的时候，只需要将提前保存在服务器缓存中（全局作用域中）的数据字典填写到下拉框之中
          * 减少了连接数据库的次数，提高了程序的运行效率
          *
          *
          * Dickey中的code为下拉框<select>中的name,DicValue中的ordNo为<option>中的value，DicValue中的value为<option>中的文本
          * 即：
          *     <select  name=DicKey.code>
                     <option value=DicValue.ordNo>DicValue.value</option>
                </select>
       * */
        System.out.println("全局作用域对象创建了===============================");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
