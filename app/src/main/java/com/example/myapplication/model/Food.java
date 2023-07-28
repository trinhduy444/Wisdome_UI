package com.example.myapplication.model;

public class Food {
    String foodId;
    String foodName;
    String foodDescription;
    String foodOriginalPrice;
    String foodDiscountedPrice;
    String foodAmount;
    String foodImage;

    public Food(String foodName, String foodDescription, String foodOriginalPrice, String foodDiscountedPrice, String foodAmount, String foodImage) {
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodOriginalPrice = foodOriginalPrice;
        this.foodDiscountedPrice = foodDiscountedPrice;
        this.foodAmount = foodAmount;
        this.foodImage = foodImage;
    }

    public String getFoodId(){
        return this.foodId;
    }

    public void setFoodId(String foodId){
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodOriginalPrice() {
        return foodOriginalPrice;
    }

    public void setFoodOriginalPrice(String foodOriginalPrice) {
        this.foodOriginalPrice = foodOriginalPrice;
    }

    public String getFoodDiscountedPrice() {
        return foodDiscountedPrice;
    }

    public void setFoodDiscountedPrice(String foodDiscountedPrice) {
        this.foodDiscountedPrice = foodDiscountedPrice;
    }

    public String getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(String foodAmount) {
        this.foodAmount = foodAmount;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodImageURL() {
//        return "http://192.168.0.6:5000/" + foodImage;
        return foodImage;
    }
}
