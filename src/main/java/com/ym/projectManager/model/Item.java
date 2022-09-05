package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Item {
    @Id
    @Column(name = "item_id", nullable = false)
    @SequenceGenerator(name = "item_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long itemId;
    private String name;
    private Integer quantity = 0;
    private Double price = 0.0;
    private String status;
    private String urlImage;
    private String variations;
    private Long listingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id")
    private ItemSection itemSection = new ItemSection(0L, "null", null);

    @Transient
    private String tempSection;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    public Item(Long itemId, String name, Integer quantity, Double price, String status, ItemSection itemSection) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.itemSection = itemSection;
    }

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(Long itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    public Item(String name, Integer quantity, Double price, String status, ItemSection itemSection) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.itemSection = itemSection;
    }

    public Long getItemId() {
        return itemId;
    }

    public Item(Long itemId, String name, Integer quantity, Double price, String status, String urlImage, String variations, Long listingId, ItemSection itemSection, Set<OrderItem> orderItems, String tempSection) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.urlImage = urlImage;
        this.variations = variations;
        this.listingId = listingId;
        this.itemSection = itemSection;
        this.orderItems = orderItems;
        this.tempSection = tempSection;
    }

    public void setItemId(Long id) {
        this.itemId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getVariations() {
        return variations;
    }

    public void setVariations(String variations) {
        this.variations = variations;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public ItemSection getItemSection() {
        return itemSection;
    }

    public void setItemSection(ItemSection itemSection) {
        this.itemSection = itemSection;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getTempSection() {
        return tempSection;
    }

    public void setTempSection(String tempSection) {
        this.tempSection = tempSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId.equals(item.itemId) && Objects.equals(name, item.name) && Objects.equals(quantity, item.quantity) && Objects.equals(price, item.price) && Objects.equals(status, item.status) && Objects.equals(urlImage, item.urlImage) && Objects.equals(variations, item.variations) && Objects.equals(listingId, item.listingId) && Objects.equals(itemSection, item.itemSection) && Objects.equals(tempSection, item.tempSection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, name, quantity, price, status, urlImage, variations, listingId, itemSection, tempSection);
    }
}
