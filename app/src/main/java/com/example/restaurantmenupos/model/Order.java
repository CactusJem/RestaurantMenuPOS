package com.example.restaurantmenupos.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    private int tableNumber;          // Tambahkan ini
    private List<OrderItem> items;
    private String status; // e.g. "PENDING", "PAID"

    public Order(String id, int tableNumber) {
        this.id = id;
        this.tableNumber = tableNumber;   // Simpan ke field
        this.items = new ArrayList<>();
        this.status = "PENDING";
    }

    public String getId() {
        return id;
    }

    public int getTableNumber() {         // Getter (opsional)
        return tableNumber;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
