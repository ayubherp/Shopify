package com.example.shopify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse {
    private String message;

    @SerializedName("carts")
    private List<Cart> cartList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}
