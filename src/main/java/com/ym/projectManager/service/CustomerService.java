package com.ym.projectManager.service;

import com.ym.projectManager.model.Customer;


import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomer(long id);
    List<Customer> getCustomerByFullName(String name);

}
