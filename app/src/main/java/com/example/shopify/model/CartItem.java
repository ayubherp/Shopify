package com.example.shopify.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

@Entity(tableName = "items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="type")
    private String type;

    @ColumnInfo(name="price")
    private double price;

    @ColumnInfo(name="amount")
    private int amount;

    @ColumnInfo(name="link_pic")
    private String linkPic;

    @ColumnInfo(name="user_id")
    private int user_id;

    public CartItem(String name, String type, double price, int amount, String linkPic)
    {
        this.name = name;
        this.price = price;
        this.type = type;
        this.amount = amount;
        this.linkPic = linkPic;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotal(){
        return price*amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getLinkPic() {
        return linkPic;
    }

    /*
    @BindingAdapter("imgURL")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop())
                .into(view);
    }

     */

}
