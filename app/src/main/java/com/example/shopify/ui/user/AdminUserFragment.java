package com.example.shopify.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.shopify.R;
import com.example.shopify.adapter.UserAdapter;
import com.example.shopify.databinding.FragmentAdminUserBinding;
import com.example.shopify.databinding.FragmentUserBinding;

public class AdminUserFragment extends Fragment {
    FragmentAdminUserBinding binding;
    public AdminUserFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_user,container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 1, GridLayoutManager.VERTICAL, false);
        binding.rvUser.setLayoutManager(gridLayoutManager);
        binding.rvUser.setAdapter(new UserAdapter(listUser, getContext()));
        binding.rvUser.setHasFixedSize(true);
        return binding.getRoot();
    }
}
