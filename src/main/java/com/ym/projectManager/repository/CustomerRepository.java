package com.ym.projectManager.repository;

import com.ym.projectManager.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> getCustomerByFullNameIsLike(String name);
}
