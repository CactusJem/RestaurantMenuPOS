package com.example.restaurantmenupos.model;

public class MenuItem {

    private String id;
    private String name;
    private double price;
    private String category;

    public MenuItem(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Overloaded constructor (example of constructor overloading)
    public MenuItem(String id, String name, double price) {
        this(id, name, price, "Uncategorized");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name + " (" + category + ") - " + price;
    }
}
