package com.example.shopify.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopify.AdminActivity;
import com.example.shopify.databinding.UserViewBinding;
import com.example.shopify.model.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolderUser> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
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
        holder.bindView(userList.get(position));
        User user = userList.get(position);

        holder.userBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                        new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data user ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (context instanceof AdminActivity)
                                    ((AdminActivity) context).deleteUser(user.getId());
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
