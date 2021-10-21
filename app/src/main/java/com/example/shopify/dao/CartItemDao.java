package com.example.shopify.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.shopify.model.CartItem;
import java.util.List;

@Dao
public interface CartItemDao {
    @Query("SELECT * FROM items")
    List<CartItem> getAll();

    @Insert
    void insertItem(CartItem item);

    @Update
    void updateItem(CartItem item);

    @Delete
    void deleteItem(CartItem item);

    @Query("SELECT * FROM items where user_id = :user_id")
    List<CartItem> getCartsByUserId(int user_id);
}
