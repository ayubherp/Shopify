package com.example.shopify.model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.shopify.BR;

public class Cart {
    private Long id;
    private Long id_user;
    private Long id_item;
    private int amount;
    private double subtotal;
    private boolean status;

    public Cart(Long id_user, Long id_item, int amount, double subtotal, boolean status)
    {
        this.id_user = id_user;
        this.id_item = id_item;
        this.amount = amount;
        this.subtotal = subtotal;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_item() {
        return id_item;
    }

    public void setId_item(Long id_item) {
        this.id_item = id_item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
