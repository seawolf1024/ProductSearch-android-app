package com.example.chapter07.entity;

public class SimilarProduct {
    private String imageUrl;
    private String title;
    private double shippingCost;
    private int daysLeft;
    private float price;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SimilarProduct(String imageUrl, String title, double shippingCost, int daysLeft, float price) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.shippingCost = shippingCost;
        this.daysLeft = daysLeft;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SimilarProduct{" +
                "imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", shippingCost=" + shippingCost +
                ", daysLeft=" + daysLeft +
                ", price=" + price +
                '}';
    }
}
