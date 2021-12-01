package com.example.shopify.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopify.R;
import com.example.shopify.database.DatabaseClient;
import com.example.shopify.databinding.AddToCartDialogBinding;
import com.example.shopify.databinding.ItemListViewBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.model.Item;
import com.example.shopify.preferences.UserPreferences;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolderItem>{
    private final ArrayList<Item> listItem;
    private Context context;
    private UserPreferences userPreferences;
    private Item item;

    public ItemAdapter(ArrayList<Item> data, Context context){
        listItem = data;
        this.context = context;
        this.userPreferences = new UserPreferences(context);
    }

    public class viewHolderItem extends RecyclerView.ViewHolder {
        ItemListViewBinding itemListBinding;
        public viewHolderItem(@NonNull ItemListViewBinding itemListBinding){
            super(itemListBinding.getRoot());
            this.itemListBinding = itemListBinding;

            this.itemListBinding.layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(Item item)
        {
            itemListBinding.setDataItem(item);
        }
    }

    @NonNull
    @Override
    public viewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemListViewBinding bindingItem = ItemListViewBinding.inflate(layoutInflater, parent, false);
        return new viewHolderItem(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderItem holder, int position) {
        holder.bindView(listItem.get(position));
        databaseClient = DatabaseClient.getInstance(context);
        holder.itemListBinding.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "onClick: " + holder.getAdapterPosition());
                item = listItem.get(holder.getAdapterPosition());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getRootView().getContext());
                AddToCartDialogBinding addDialogBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(v.getRootView().getContext()), R.layout.add_to_cart_dialog, null, false
                        );
                addDialogBinding.setDataItem(item);
                alertDialog.setView(addDialogBinding.getRoot());
                addDialogBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCartItem();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @BindingAdapter("imgURL")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop())
                .into(view);
    }

    private void addCartItem(){
        class AddCartItem extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Cart cart = new Cart(item.getName(), item.getType(), item.getPrice(),1,item.getLinkPic());
                cart.setUser_id(userPreferences.getUserLogin().getId());

                DatabaseClient.getInstance(context.getApplicationContext())
                        .getDatabase()
                        .listCartItemDao()
                        .insertItem(cart);

                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(context.getApplicationContext(), "Item added to Cart", Toast.LENGTH_SHORT).show();
            }

        }
        AddCartItem addCartItem = new AddCartItem(  );
        addCartItem.execute();
    }

}
