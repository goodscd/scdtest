package com.cd.crm.workbench.service.impl;

import com.cd.crm.exception.ClueException;
import com.cd.crm.settings.dao.UserDao;
import com.cd.crm.settings.domian.User;
import com.cd.crm.utils.DateTimeUtil;
import com.cd.crm.utils.UUIDUtil;
import com.cd.crm.workbench.dao.*;
import com.cd.crm.workbench.domian.*;
import com.cd.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service(value = "clueService")
public class ClueServiceImpl implements ClueService {
    //线索相关表
    @Autowired
    @Qualifier(value = "clueDao")
    private ClueDao clueDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name="clueActivityRelationDao")
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource(name="activityDao")
    private ActivityDao activityDao;
    @Autowired
    @Qualifier(value = "clueRemarkDao")
    private ClueRemarkDao clueRemarkDao;
    //客户相关表
    @Resource(name = "customerDao")
    private CustomerDao customerDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource(name = "contactsRemarkDao")
    private ContactsRemarkDao contactsRemarkDao;
    @Resource(name = "contactsActivityRelationDao")
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    //联系人相关表
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;




    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();

        return userList;
    }

    @Override
    public boolean saveClue(Clue clue) throws ClueException {
        int i=clueDao.saveClue(clue);
        boolean flag=true;
        if (i!=1) {
            flag=false;
            throw new ClueException("添加失败");
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue=clueDao.detail(id);
        return clue;
    }

    @Override
    public List<Activity> getActivityListClueId(String clueId) {
        List<Activity> activities= activityDao.getActivityListClueId(clueId);
        return activities;
    }

    @Override
    public boolean delClueActivityRelation(String id) throws ClueException {
        int i=clueActivityRelationDao.delClueActivityRelation(id);
        boolean flag=true;
        if(i!=1){
            flag=false;
            throw  new ClueException("删除失败");
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityByList(Map<String, String> map) {
        List<Activity> list=activityDao.getActivityByList(map);
        return list;
    }
    @Transactional
    @Override
    public boolean getClueActivityRelation(Map<String, Object> map) throws ClueException {
        String[] activityIds = (String[]) map.get("activityIds");
        String clueId = (String) map.get("clueId");
        int i=0;
        ClueActivityRelation clueActivityRelation=new ClueActivityRelation();
        for (String activityId : activityIds) {
            String id=UUIDUtil.getUUID();
            clueActivityRelation.setId(id);
            clueActivityRelation.setActivityId(activityId);
            clueActivityRelation.setClueId(clueId);
            i=clueActivityRelationDao.getClueActivityRelation(clueActivityRelation);
        }
        boolean flag=true;
        if (i!=1) {
            flag=false;
            throw  new ClueException("添加失败");
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityAndClueId(String aname, String clueId) {
        List<Activity> activities=activityDao.getActivityAndClueId(aname,clueId);
        return activities;
    }
    @Transactional
    @Override
    public boolean convert(String clueId, Tran tran, String createBy) throws ClueException {
        boolean flag=true;
        String createTime= DateTimeUtil.getSysTime();
        Clue clue=clueDao.getById(clueId);
        String company = clue.getCompany();
        /*查询客户表里面是否有这个公司的信息，如果没有则新建一条客户信息*/
        Customer customer =customerDao.getCustomerByName(company);
        if (customer==null) {
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            /*添加客户*/
            int i=customerDao.save(customer);
            if (i!=1) {
                flag=false;
                throw new ClueException("客户信息表添加失败");
            }
        }
        /*通过线索对象提提取联系人信息，保存联系人*/
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        System.out.println(contactsDao);
        int i1=contactsDao.save(contacts);

        if (i1!=1){
            flag=false;
            throw new ClueException("联系人信息表添加失败");
        }
        /*取得线索备注表中的信息*/
        List<ClueRemark> clueRemarks=clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarks) {
            //取出备注信息,添加客户备注
            String noteContent=clueRemark.getNoteContent();
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(createTime);
            int i3=customerRemarkDao.save(customerRemark);
            if (i3!=1) {
                flag=false;
                throw  new ClueException("客户备注添加失败");
            }
            /*创建联系人备注*/
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateTime(createTime);
            int i4=contactsRemarkDao.save(contactsRemark);
            if (i4 !=1) {
                flag=false;
                throw  new ClueException("客户备注添加失败");

            }

        }
        List<ClueActivityRelation> clueActivityRelations=clueActivityRelationDao.getActivityRelationList(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int i5=contactsActivityRelationDao.save(contactsActivityRelation);
            if (i5!=1) {
                flag=false;
                throw  new ClueException("添加失败");
            }

        }
        /*添加交易*/
        if(tran!=null){
            tran.setCustomerId(customer.getId());
            tran.setContactsId(contacts.getId());
            tran.setDescription(clue.getDescription());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setSource(clue.getSource());
            tran.setContactSummary(clue.getContactSummary());
            tran.setOwner(clue.getOwner());
            int i5=tranDao.save(tran);
            if (i5!=1) {
                flag=false;
                throw new ClueException("交易信息表添加失败");
            }
            /*添加交易历史*/
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());
            /*添加交易历史*/
            int i6=tranHistoryDao.save(tranHistory);
            if (i6!=1) {
                flag=false;
                throw new ClueException("交易历史信息表添加失败");
            }
        }
        /*删除线索备注*/
        for (ClueRemark clueRemark : clueRemarks) {
            int i7=clueRemarkDao.delete(clueRemark);
            if (i7!=1) {
                flag=false;
                throw new ClueException("线索备注表删除失败");
            }
        }
        /*删除线索和市场活动的关联关系*/
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            int i8=clueActivityRelationDao.delete(clueActivityRelation);
            if (i8!=1) {
                flag=false;
                throw new ClueException("市场活动关联线索表删除失败");
            }
        }
        /*删除线索*/
        int i9=clueDao.delete(clueId);
        if (i9!=1) {
            flag=false;
            throw new ClueException("线索表删除失败");
        }


        return flag;
    }



}
