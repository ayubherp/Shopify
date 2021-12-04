package com.example.shopify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemResponse {
    private String message;

    @SerializedName("item")
    private Item item;

    @SerializedName("data")
    private List<Item> itemList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
