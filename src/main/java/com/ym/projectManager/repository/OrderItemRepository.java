package com.ym.projectManager.repository;

import com.ym.projectManager.model.Item;
import com.ym.projectManager.model.Order;
import com.ym.projectManager.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderEqualsAndItemEquals(Order order, Item item);
}
