package com.example.shopify.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    private String message;

    @SerializedName("user")
    private List<User> userList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
