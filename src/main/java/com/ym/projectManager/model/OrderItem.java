package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


@Entity(name="OrderItemEntity")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="order_item")
public class OrderItem {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "order_item_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_seq")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer count;
    private Double price;
    private Double sumValue;

    public OrderItem(Item item, Order order, Integer count, Double price, Double sumValue) {
        this.item = item;
        this.order = order;
        this.count = count;
        this.price = price;
        this.sumValue = sumValue;
    }

    public OrderItem() {

    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long id) {
        this.orderItemId = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getSumValue() {
        return sumValue;
    }

    public void setSumValue(Double sumValue) {
        this.sumValue = sumValue;
    }

    public Item getItem() {
        return item;
    }

    public Order getOrder() {
        return order;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }




}
