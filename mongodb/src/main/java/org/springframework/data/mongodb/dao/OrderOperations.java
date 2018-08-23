package org.springframework.data.mongodb.dao;

import org.springframework.data.mongodb.entity.Order;

import java.util.List;

public interface OrderOperations {

    List<Order> findOrdersByType(String t);
}
