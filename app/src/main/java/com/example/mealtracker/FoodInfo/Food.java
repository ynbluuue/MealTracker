package com.example.mealtracker.FoodInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Food {
    public String foodType;
    public String foodPlace;
    public String foodName;
    public String foodReview;
    public String imageName;
    public long foodPrice;
    public String dateTime;
    public String id;

    public Food(String id, String foodType, String foodPlace, String foodName, String foodReview, String imageName, long foodPrice, String dateTime) {
        this.id = id;
        this.foodType = foodType;
        this.foodPlace = foodPlace;
        this.foodName = foodName;
        this.foodReview = foodReview;
        this.imageName = imageName;
        this.foodPrice = foodPrice;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodType() {
        return foodType;
    }

    public long getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(long foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodPlace() {
        return foodPlace;
    }

    public void setFoodPlace(String foodPlace) {
        this.foodPlace = foodPlace;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodReview() {
        return foodReview;
    }

    public void setFoodReview(String foodReview) {
        this.foodReview = foodReview;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
