package com.example.shopify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse {
    private String message;

    @SerializedName("cart")
    private Cart cart;

    @SerializedName("data")
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
