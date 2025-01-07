package com.example.hotel;

public class Discount {
    private int discountID;
    private String discountName;
    private String description;
    private double discountPercentage;
    private String startDate;
    private String endDate;
    private String applicableRoomType;

    public Discount(int discountID, String discountName, String description, double discountPercentage, String startDate, String endDate, String applicableRoomType) {
        this.discountID = discountID;
        this.discountName = discountName;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.applicableRoomType = applicableRoomType;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getApplicableRoomType() {
        return applicableRoomType;
    }

    public void setApplicableRoomType(String applicableRoomType) {
        this.applicableRoomType = applicableRoomType;
    }
}
