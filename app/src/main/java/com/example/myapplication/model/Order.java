package com.example.myapplication.model;

import java.util.List;

public class Order {

    String order_Id;
    String customer_Id;
    String shop_name;
    String status;
    double totalPrice;
    String date_order;
    String time_order;
    private List<String> food_name;
    private List<Integer> food_amount;

    public Order(String order_Id, String customer_Id, String shop_name, String status, double totalPrice, String date_order, String time_order, List<String> food_name, List<Integer> food_amount) {
        this.order_Id = order_Id;
        this.customer_Id = customer_Id;
        this.shop_name = shop_name;
        this.status = status;
        this.totalPrice = totalPrice;
        this.date_order = date_order;
        this.time_order = time_order;
        this.food_name = food_name;
        this.food_amount = food_amount;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getCustomer_Id() {
        return customer_Id;
    }

    public void setCustomer_Id(String customer_Id) {
        this.customer_Id = customer_Id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public String getTime_order() {
        return time_order;
    }

    public void setTime_order(String time_order) {
        this.time_order = time_order;
    }

    public List<String> getFood_name() {
        return food_name;
    }

    public void setFood_name(List<String> food_name) {
        this.food_name = food_name;
    }

    public List<Integer> getFood_amount() {
        return food_amount;
    }

    public void setFood_amount(List<Integer> food_amount) {
        this.food_amount = food_amount;
    }
}
