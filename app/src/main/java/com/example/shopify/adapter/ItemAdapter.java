package com.example.shopify.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.shopify.MainActivity;
import com.example.shopify.R;
import com.example.shopify.databinding.AddToCartDialogBinding;
import com.example.shopify.databinding.ItemViewBinding;
import com.example.shopify.model.Item;
import com.example.shopify.ui.item.ItemFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolderItem>
        implements Filterable {
    private List<Item> itemList, filteredItemList;
    private Context context;
    private Item item;

    public ItemAdapter(List<Item> itemList, Context context){
        this.itemList = itemList;
        filteredItemList = new ArrayList<>(itemList);
        this.context = context;
    }

    public class viewHolderItem extends RecyclerView.ViewHolder {
        ItemViewBinding binding;
        public viewHolderItem(@NonNull ItemViewBinding binding){
            super(binding.getRoot());
            this.binding = binding;

            this.binding.layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(Item item)
        {
            binding.setItem(item);
        }
    }

    @NonNull
    @Override
    public viewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemViewBinding bindingItem = ItemViewBinding.inflate(layoutInflater, parent, false);
        return new viewHolderItem(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderItem holder, int position) {
        Item item = filteredItemList.get(position);
        holder.binding.layoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getRootView().getContext());
                AddToCartDialogBinding addDialogBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(v.getRootView().getContext()), R.layout.add_to_cart_dialog, null, false
                        );
                addDialogBinding.setItemData(item);
                //convert image to byte
                byte[] imageByteArray = Base64.decode(item.getImage(),Base64.DEFAULT);
                Glide.with(context.getApplicationContext())
                        .asBitmap()
                        .load(imageByteArray)
                        .placeholder(R.drawable.no_image)
                        .into(addDialogBinding.imageDialog);
                alertDialog.setView(addDialogBinding.getRoot());
                //add to Cart
                addDialogBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof MainActivity)
                            ((MainActivity) context).addItemToCart(item);
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
        holder.binding.txtMerk.setText(item.getName());
        holder.binding.txtType.setText(item.getType());
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.binding.txtPrice.setText(rupiah.format(item.getPrice()));
        byte[] imageByteArray = Base64.decode(item.getImage(),Base64.DEFAULT);
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imageByteArray)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.imgView);
        holder.bindView(filteredItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredItemList.size();
    }

    public void setItemList(List<Item> itemList) {
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
                filteredItemList.clear();
                filteredItemList.addAll((List<Item>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}
