package com.example.shopify.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopify.database.DatabaseClient;
import com.example.shopify.databinding.CartItemViewBinding;
import com.example.shopify.model.CartItem;
import com.example.shopify.preferences.UserPreferences;

import java.util.List;

public class ListCartItemAdapter extends RecyclerView.Adapter<ListCartItemAdapter.viewHolderItem>{
    private List<CartItem> cartItemList;
    private Context context;
    private DatabaseClient databaseClient;
    private UserPreferences userPreferences;

    public ListCartItemAdapter(List<CartItem> data, Context context){
        this.cartItemList = data;
        this.context = context;
        this.userPreferences= new UserPreferences(context);
        notifyDataSetChanged();
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
        public void bindView(CartItem item)
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
        holder.bindView(cartItemList.get(position));
        CartItem item = cartItemList.get(position);
        databaseClient = DatabaseClient.getInstance(context);

        holder.cartItemBinding.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getAmount()==1)
                {
                    databaseClient.getDatabase().listCartItemDao().deleteItem(item);
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    cartItemList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                else if(item.getAmount()>1)
                {
                    int temp;
                    temp = item.getAmount() - 1;
                    item.setAmount(temp);
                    Toast.makeText(context, "Item reduced", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
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
                    Toast.makeText(context, "Item added ", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

}
