package com.example.chapter07.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductCard {
    String itemId = null;
    String itemImage = null;
    String itemTitle = null;
    float itemPrice = 0;
    String itemZipcode = null;
    float itemShippingCost = 0;

    String conditionId = null;

    public ProductCard(String itemId, String itemImage, String itemTitle, float itemPrice, String itemZipcode, float itemShippingCost, String conditionId) {
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
        this.itemZipcode = itemZipcode;
        this.itemShippingCost = itemShippingCost;
        this.conditionId = conditionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemZipcode() {
        return itemZipcode;
    }

    public void setItemZipcode(String itemZipcode) {
        this.itemZipcode = itemZipcode;
    }

    public float getItemShippingCost() {
        return itemShippingCost;
    }

    public void setItemShippingCost(float itemShippingCost) {
        this.itemShippingCost = itemShippingCost;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    @Override
    public String toString() {
        return "ProductCard{" +
                "itemId='" + itemId + '\'' +
                ", itemImage='" + itemImage + '\'' +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemZipcode='" + itemZipcode + '\'' +
                ", itemShippingCost=" + itemShippingCost +
                ", conditionId='" + conditionId + '\'' +
                '}';
    }
}
