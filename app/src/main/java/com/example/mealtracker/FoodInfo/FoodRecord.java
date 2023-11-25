package com.example.mealtracker.FoodInfo;

public class FoodRecord {
    private String dateTime;
    private String foodName;
    private String foodPlace;
    private String foodReview;
    private String imageName;
    private String foodType;
    private long foodPrice;
    private String id;

    // 기본 생성자가 필요합니다.
    public FoodRecord() {
    }

    public FoodRecord(String date, String foodName) {
        this.dateTime = date;
        this.foodName = foodName;
    }

    public FoodRecord(String date, String foodName, String foodPlace, String foodReview, String imageName, String foodType, long foodPrice, String id) {
        this.dateTime = date;
        this.foodName = foodName;
        this.foodPlace = foodPlace;
        this.foodReview = foodReview;
        this.imageName = imageName;
        this.foodType = foodType;
        this.foodPrice = foodPrice;
        this.id = id;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPlace() {
        return foodPlace;
    }

    public void setFoodPlace(String foodPlace) {
        this.foodPlace = foodPlace;
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

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(long foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getFoodName() {
        return foodName;
    }
}
