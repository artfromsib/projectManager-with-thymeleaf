package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        this.orderItems.stream().forEach(item -> items.add(item.getItem()));
        return items;
    }

    public String getShippingDateFormat() {
        return shippingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}



