package com.example.shopify.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopify.database.DatabaseClient;
import com.example.shopify.databinding.CartItemViewBinding;
import com.example.shopify.databinding.FragmentCartItemBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.cart.CartActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolderItem>{
    private List<Cart> cartList;
    private Context context;
    private UserPreferences userPreferences;
    private FragmentCartItemBinding currentFragment;

    public CartAdapter(List<Cart> data, Context context){
        this.cartList = data;
        this.context = context;
        this.userPreferences= new UserPreferences(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_produk, parent, false);

        return new ViewHolder(view);
    }
    public class viewHolderItem extends RecyclerView.ViewHolder {
        CartItemViewBinding cartItemBinding;
        public viewHolderItem(@NonNull CartItemViewBinding cartItemBinding){
            super(cartItemBinding.getRoot());
            this.cartItemBinding = cartItemBinding;

            this.cartItemBinding.cartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(Cart item)
        {
            cartItemBinding.setItems(item);
        }
    }

    @NonNull
    @Override
    public viewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CartItemViewBinding bindingItem = CartItemViewBinding.inflate(layoutInflater, parent, false);
        return new viewHolderItem(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderItem holder, int position) {
        holder.bindView(cartList.get(position));
        Cart item = cartList.get(position);
        databaseClient = DatabaseClient.getInstance(context);

        holder.cartItemBinding.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getAmount()==1)
                {
                    databaseClient.getDatabase().listCartItemDao().deleteItem(item);
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    cartList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                else if(item.getAmount()>1)
                {
                    int temp;
                    temp = item.getAmount() - 1;
                    item.setAmount(temp);
                    databaseClient.getDatabase().listCartItemDao().updateItem(item);
                    cartList.clear();
                    cartList.addAll(databaseClient.getDatabase()
                            .listCartItemDao().getCartsByUserId(
                                    userPreferences.getUserLogin().getId()));
                    Toast.makeText(context, "Item reduced", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, CartActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });
        holder.cartItemBinding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getAmount()>=50)
                {
                    Toast.makeText(context, "Can't add more", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int temp;
                    temp = item.getAmount() + 1;
                    item.setAmount(temp);
                    databaseClient.getDatabase().listCartItemDao().updateItem(item);
                    cartList.clear();
                    cartList.addAll(databaseClient.getDatabase()
                            .listCartItemDao().getCartsByUserId(
                                    userPreferences.getUserLogin().getId()));
                    Toast.makeText(context, "Item added ", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, CartActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public List<Cart> getItems(){ return cartList;}

    public void removeCartItems(){
        Cart item;
        for (int i = 0; i< cartList.size(); i++){
            item = cartList.get(i);
            databaseClient.getDatabase().listCartItemDao().deleteItem(item);
        }
        cartList.clear();
        notifyDataSetChanged();
    }

}
