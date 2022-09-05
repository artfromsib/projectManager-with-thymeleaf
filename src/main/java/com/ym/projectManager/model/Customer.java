package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="customer", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @Column(name = "customer_id", nullable = false)
    @SequenceGenerator(name = "customer_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    private Long customerId;
    private Long userOtherId;
    @NotBlank
    private String fullName;
    private String address;
    private Integer countOrders = 1;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Order> customerOrders = new HashSet<>();

    public Customer(Long customerId, String fullName, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.address = address;
    }

    public Customer(Long customerId, Long userOtherId, String fullName, String address, Integer countOrders, Set<Order> customerOrders) {
        this.customerId = customerId;
        this.userOtherId = userOtherId;
        this.fullName = fullName;
        this.address = address;
        this.countOrders = countOrders;
        this.customerOrders = customerOrders;
    }

    public Customer() {

    }

    public Customer(String fullName, String address) {
        this.fullName = fullName;
        this.address = address;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long id) {
        this.customerId = id;
    }

    public Long getUserOtherId() {
        return userOtherId;
    }

    public void setUserOtherId(Long user_id) {
        this.userOtherId = user_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String full_name) {
        this.fullName = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCountOrders() {
        return countOrders;
    }

    public void setCountOrders(Integer count_orders) {
        this.countOrders = count_orders;
    }

    public Set<Order> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(Set<Order> customerOrders) {
        this.customerOrders = customerOrders;
    }

    @Override
    public String toString() {
        return "To: " + fullName + ", address: " + address;
    }
}
