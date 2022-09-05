package com.ym.projectManager.model.templateWrap.order;

import com.ym.projectManager.model.Customer;
import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.Order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderTempl {

    private Order order;
    private List<Item> itemsOfOrder = new ArrayList<>();
    private Item newItem;
    private List<SelectItemsFromDB> itemsFromDB = new ArrayList<>();
    private String findByItemName;
    private String searchAswerItem;
    @Valid
    @NotNull
    private Customer customer;
    private List<Customer> customersFromDB = new ArrayList<>();
    private String findByCustomerName;
    private String searchAnswerCustomer;

    public OrderTempl(Order order, List<Item> itemsOfOrder, Item newItem, List<SelectItemsFromDB> itemsFromDB, String findByItemName, String searchAnswerItem, Customer customer, List<Customer> customersFromDB, String findByCustomerName, String searchAnswerCustomer) {
        this.order = order;
        this.itemsOfOrder = itemsOfOrder;
        this.newItem = newItem;
        this.itemsFromDB = itemsFromDB;
        this.findByItemName = findByItemName;
        this.searchAswerItem = searchAnswerItem;
        this.customer = customer;
        this.customersFromDB = customersFromDB;
        this.findByCustomerName = findByCustomerName;
        this.searchAnswerCustomer = searchAnswerCustomer;
    }

    public List<Item> getItemsOfOrder() {
        return itemsOfOrder;
    }

    public void setOrderItems(List<Item> items) {
        this.itemsOfOrder = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getNewItem() {
        return newItem;
    }

    public void setNewItem(Item newItem) {
        this.newItem = newItem;
    }

    public OrderTempl() {

    }

    public List<Item> getSelectedItemsFromDB(){
        List <Item> selectedItemsFromDB = new ArrayList<>();
        itemsFromDB.stream().filter(item -> item.isSelected())
                .forEach(item->{selectedItemsFromDB.add(item.getItem());});
        return selectedItemsFromDB;
    }

    public void setItemsOfOrder(List<Item> itemsOfOrder) {
        this.itemsOfOrder = itemsOfOrder;
    }

    public OrderTempl(Order order, List<Item> itemsOfOrder, Item item, Customer customer) {
        this.order = order;
        this.itemsOfOrder = itemsOfOrder;
        this.newItem = item;
        this.customer = customer;
    }

    public List<SelectItemsFromDB> getItemsFromDB() {
        return itemsFromDB;
    }

    public void setItemsFromDB(List<SelectItemsFromDB> itemsFromDB) {
        this.itemsFromDB = itemsFromDB;
    }

    public void setItemsFromDBService(List<Item> itemsFromDB) {
        List <SelectItemsFromDB> tmp = new ArrayList<SelectItemsFromDB>();
        if (itemsFromDB.size()>0) {
            itemsFromDB.forEach(item -> {tmp.add(new SelectItemsFromDB(false,item));});
            this.itemsFromDB = tmp;
        }
        else this.itemsFromDB = new ArrayList<>();

    }

    public String getFindByItemName() {
        return findByItemName;
    }

    public void setFindByItemName(String findByItemName) {
        this.findByItemName = findByItemName;
    }

    public String getSearchAswerItem() {
        return searchAswerItem;
    }

    public void setSearchAswerItem(String searchAswerItem) {
        this.searchAswerItem = searchAswerItem;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Customer> getCustomersFromDB() {
        return customersFromDB;
    }

    public void setCustomersFromDB(List<Customer> customersFromDB) {
        this.customersFromDB = customersFromDB;
    }

    public String getFindByCustomerName() {
        return findByCustomerName;
    }

    public void setFindByCustomerName(String findByCustomerName) {
        this.findByCustomerName = findByCustomerName;
    }

    public String getSearchAnswerCustomer() {
        return searchAnswerCustomer;
    }

    public void setSearchAnswerCustomer(String searchAnswerCustomer) {
        this.searchAnswerCustomer = searchAnswerCustomer;
    }
}
