package com.example.myapplication.model;

public class OrderDetail {
    String orderdetail_id;
    String order_id;
    String shipper_id;
    String note;

    String address_restaurant;
    String address_customer;
    String payment_name;
    double rating;

    public OrderDetail(String orderdetail_id, String order_id, String shipper_id, String note, String address_restaurant, String address_customer, String payment_name, double rating) {
        this.orderdetail_id = orderdetail_id;
        this.order_id = order_id;
        this.shipper_id = shipper_id;
        this.note = note;
        this.address_restaurant = address_restaurant;
        this.address_customer = address_customer;
        this.payment_name = payment_name;
        this.rating = rating;
    }

    public String getOrderdetail_id() {
        return orderdetail_id;
    }

    public void setOrderdetail_id(String orderdetail_id) {
        this.orderdetail_id = orderdetail_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getShipper_id() {
        return shipper_id;
    }

    public void setShipper_id(String shipper_id) {
        this.shipper_id = shipper_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress_restaurant() {
        return address_restaurant;
    }

    public void setAddress_restaurant(String address_restaurant) {
        this.address_restaurant = address_restaurant;
    }

    public String getAddress_customer() {
        return address_customer;
    }

    public void setAddress_customer(String address_customer) {
        this.address_customer = address_customer;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
