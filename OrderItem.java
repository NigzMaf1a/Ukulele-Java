package com.example.theukuleleband.modules.storeman;

public class OrderItem {
    private String supplierName;
    private String description;
    private String brand;

    public OrderItem(String supplierName, String description, String brand) {
        this.supplierName = supplierName;
        this.description = description;
        this.brand = brand;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }
}
