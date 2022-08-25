package com.ym.projectManager.service.impl;



import com.ym.projectManager.repository.*;
import com.ym.projectManager.service.OrderService;
import com.ym.projectManager.model.*;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final ParcelRepository parcelRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ItemRepository itemRepository, CustomerRepository customerRepository,
                            ParcelRepository parcelRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
        this.parcelRepository = parcelRepository;
    }

    @Override
    public Order saveOrderWithItemsCustomerAndParcel(Order order, List<Item> items, Customer customer) {
        Order newOrder;

        if (customer.getCustomerId() == null) {
            Customer newCustomer = customerRepository.save(customer);
            order.setCustomer(newCustomer);
        } else {
            order.setCustomer(customerRepository.saveAndFlush(customer));
        }

        if (order.getParcel().getTrackNumber().length() > 1)
            if (order.getParcel().getParcelId() == null) {
                Parcel parcel = parcelRepository.save(order.getParcel());
                order.setParcel(parcel);
            } else {
                parcelRepository.saveAndFlush(order.getParcel());
            }
        else {
            order.setParcel(null);
        }

        if (order.getOrderId() == null) {
            newOrder = orderRepository.save(order);
        } else {
            newOrder = order;
        }
        AtomicReference<Double> orderTotal = new AtomicReference<>(0.0);
        AtomicInteger orderValue = new AtomicInteger();
        Order finalNewOrder = newOrder;
        items.forEach(item -> {
            Item newItem;

            if (item.getItemId() == null) {
                newItem = itemRepository.save(item);
            } else {
                newItem = itemRepository.saveAndFlush(item);
            }
            orderTotal.getAndSet((orderTotal.get() + (item.getPrice() * item.getQuantity())));
            orderValue.getAndSet(orderValue.get() + item.getQuantity());
            OrderItem orderItem = new OrderItem(newItem, finalNewOrder, item.getQuantity(),
                    item.getPrice(), item.getQuantity() * item.getPrice());
            if (orderItemRepository.findByOrderEqualsAndItemEquals(finalNewOrder, newItem) == null)
                orderItemRepository.save(orderItem);

        });
        newOrder.setOrderTotal(orderTotal.get());
        newOrder.setOrderValue(orderValue.get());
        newOrder = orderRepository.saveAndFlush(newOrder);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    @Override
    public Optional <Order> getOrderById(Long id) {
        return Optional.ofNullable(orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Order with \"%s\" doesn't exist.", id))));
    }

    @Override
    public void deleteOrderById(Long id) {
        getOrderOrThrowException(id);
        orderRepository.deleteById(id);
    }

    private void getOrderOrThrowException(Long id) {
        orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Order with \"%s\" doesn't exist.", id)));
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {

        return orderRepository.findByStatusOrderByDateSaleDesc(status);
    }

    @Override
    public void setOrderStatus(Long id, String status) {
        Order order = orderRepository.getById(id);
        order.setStatus(status);
        //orderRepository.saveAndFlush(order);
        if (status.equals("COMPLETE")) {
            order.setShippingDate(LocalDateTime.now());
        }
        orderRepository.saveAndFlush(order);
    }

    @Override
    public void addTrackNumberToOrderAndSaveParcel(Long orderId, String trackNum) {
        Parcel parcel = parcelRepository.findFirstByTrackNumber(trackNum);
        if (parcel == null) {
            parcel = parcelRepository.save(new Parcel(trackNum));
        } else {
            parcelRepository.saveAndFlush(parcel);
        }
        Order order = orderRepository.getById(orderId);
        order.setParcel(parcel);
        orderRepository.saveAndFlush(order);
    }



    @Override
    public Parcel setOrderDelivered(Long parcelId) {
        Parcel parcel = parcelRepository.getById(parcelId);
        parcel.setDelivered(true);
        return parcelRepository.saveAndFlush(parcel);
    }


}
