package com.example.shopify.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopify.AdminActivity;
import com.example.shopify.databinding.UserViewBinding;
import com.example.shopify.model.Item;
import com.example.shopify.model.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolderUser>
        implements Filterable {

    private List<User> userList, filteredUserList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        filteredUserList = new ArrayList<>(userList);
        this.context = context;
    }

    public class viewHolderUser extends RecyclerView.ViewHolder {
        UserViewBinding userBinding;
        public viewHolderUser(@NonNull UserViewBinding userBinding){
            super(userBinding.getRoot());
            this.userBinding = userBinding;

            this.userBinding.layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(User user)
        {
            userBinding.setDataUser(user);
        }
    }
    @NonNull
    @Override
    public viewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserViewBinding binding = UserViewBinding.inflate(inflater, parent, false);
        return new UserAdapter.viewHolderUser(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderUser holder, int position) {
        User user = filteredUserList.get(position);
        holder.bindView(filteredUserList.get(position));

        holder.userBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                        new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Are you sure you want to delete this user's data?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AdminActivity) context).deleteUser(user.getId());
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredUserList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        filteredUserList = new ArrayList<>(userList);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<User> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty()) {
                    filtered.addAll(userList);
                } else {
                    for (User user : userList) {
                        if (user.getName().toLowerCase().contains(charSequenceString.toLowerCase()))
                            filtered.add(user);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserList.clear();
                filteredUserList.addAll((List<User>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}
