package com.example.shopify.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.shopify.dao.CartItemDao;
import com.example.shopify.dao.UserDao;
import com.example.shopify.model.CartItem;
import com.example.shopify.model.User;

@Database(entities = {CartItem.class, User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartItemDao listCartItemDao();
    public abstract UserDao userDao();
}