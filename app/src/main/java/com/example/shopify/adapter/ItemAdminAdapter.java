package com.example.shopify.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopify.AdminActivity;
import com.example.shopify.R;
import com.example.shopify.databinding.ItemAdminViewBinding;
import com.example.shopify.databinding.ItemViewBinding;
import com.example.shopify.model.Item;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.item.AddEditActivity;
import com.example.shopify.ui.item.AdminItemFragment;
import com.example.shopify.ui.item.ItemFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdminAdapter extends RecyclerView.Adapter<ItemAdminAdapter.viewHolderItem>
        implements Filterable {
    private List<Item> itemList, filteredItemList;
    private Context context;
    private Item item;
    private UserPreferences userPreferences;

    public ItemAdminAdapter(List<Item> data, Context context){
        itemList = data;
        this.context = context;
        userPreferences = new UserPreferences(context);
    }

    public class viewHolderItem extends RecyclerView.ViewHolder {
        ItemAdminViewBinding binding;
        public viewHolderItem(@NonNull ItemAdminViewBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            this.binding.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(Item item)
        {
            binding.setDataItem(item);
        }
    }

    @NonNull
    @Override
    public viewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAdminViewBinding bindingItem = ItemAdminViewBinding.inflate(layoutInflater, parent, false);
        return new viewHolderItem(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderItem holder, int position) {
        Item item = filteredItemList.get(position);
        holder.binding.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "onClick: " + holder.getAdapterPosition());
                ((AdminActivity)context).toEditItemActivity(item.getId());
            }
        });
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                        new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Are you sure you want to delete this item data? ")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AdminActivity) context).deleteItem(item.getId());
                            }
                        })
                        .show();
            }
        });
        holder.binding.tvName.setText(item.getName());
        holder.binding.tvType.setText(item.getType());
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.binding.tvPrice.setText(rupiah.format(item.getPrice()));
        byte[] imageByteArray = Base64.decode(item.getImage(),Base64.DEFAULT);
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imageByteArray)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.ivImage);
        holder.bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProdukList(List<Item> itemList) {
        this.itemList = itemList;
        filteredItemList = new ArrayList<>(itemList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Item> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty()) {
                    filtered.addAll(itemList);
                } else {
                    for (Item item : itemList) {
                        if (item.getName().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItemList.addAll((List<Item>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}
