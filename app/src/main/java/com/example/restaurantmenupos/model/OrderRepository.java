package com.example.restaurantmenupos.model;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private static OrderRepository instance;
    private final List<Order> orders;

    private OrderRepository() {
        orders = new ArrayList<>();
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public int getOrderCount() {
        return orders.size();
    }
}
