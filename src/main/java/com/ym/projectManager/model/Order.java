package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "order1")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    @Id
    @Column(name = "order_id", nullable = false)
    @SequenceGenerator(name = "order_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    private Long orderId;
    private LocalDateTime dateSale = LocalDateTime.now();
    private LocalDateTime datePosted;
    private Long etsyOrderId;
    private Integer orderValue;
    @Column(name = "ship_date")
    private LocalDateTime shippingDate;
    private Long couponId;
    private boolean useCoupon = false;
    private Double discountAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Double deliveryCost;
    private double orderTotal = 0.0;
    private Integer countItems;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    @NotNull
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;

    public Order() {

    }

    public Order(Long orderId, LocalDateTime dateSale, LocalDateTime datePosted, Long etsyOrderId, Integer orderValue, Long couponId, boolean useCoupon, Double discountAmount, Double deliveryCost, double orderTotal, Integer countItems, String status, Parcel parcel, LocalDateTime shippingDate, Set<OrderItem> orderItems, Customer customer) {
        this.orderId = orderId;
        this.dateSale = dateSale;
        this.datePosted = datePosted;
        this.etsyOrderId = etsyOrderId;
        this.orderValue = orderValue;
        this.couponId = couponId;
        this.useCoupon = useCoupon;
        this.discountAmount = discountAmount;
        this.deliveryCost = deliveryCost;
        this.orderTotal = orderTotal;
        this.countItems = countItems;
        this.status = status;
        this.parcel = parcel;
        this.shippingDate = shippingDate;
        this.orderItems = orderItems;
        this.customer = customer;
    }

    public Order(Long orderId, String status, Parcel parcel) {
        this.orderId = orderId;
        this.status = status;
        this.parcel = parcel;
    }

    public Order(Long orderId, Customer customer, String status, Set<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customer = customer;
        this.status = status;
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long id) {
        this.orderId = id;
    }

    public LocalDateTime getDateSale() {
        return dateSale;
    }

    public void setDateSale(LocalDateTime dateSale) {
        this.dateSale = dateSale;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public Long getEtsyOrderId() {
        return etsyOrderId;
    }

    public void setEtsyOrderId(Long etsyOrderId) {
        this.etsyOrderId = etsyOrderId;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public boolean isUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(boolean useCoupon) {
        this.useCoupon = useCoupon;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getCountItems() {
        return countItems;
    }

    public void setCountItems(int countItems) {
        this.countItems = countItems;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        this.orderItems.stream().forEach(item -> items.add(item.getItem()));
        return items;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    public void setDeliveryCost(Double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setCountItems(Integer countItems) {
        this.countItems = countItems;
    }

    public LocalDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingDateFormat(){
        return shippingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}



