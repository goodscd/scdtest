package com.cd.crm.workbench.dao;

import com.cd.crm.workbench.domian.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);
}
