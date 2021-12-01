package com.example.shopify.ui.item;

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
import com.example.shopify.adapter.ItemAdapter;
import com.example.shopify.databinding.ItemListRvBinding;
import com.example.shopify.model.Item;
import com.example.shopify.model.ListItem;

import java.util.ArrayList;

public class ItemFragment extends Fragment {
    public ItemFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ItemListRvBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_item, container, false);
        ArrayList<Item> listItem = new ListItem().item;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        binding.rvItemList.setLayoutManager(gridLayoutManager);
        binding.rvItemList.setAdapter(new ItemAdapter(listItem, getContext()));
        binding.rvItemList.setHasFixedSize(true);
        return binding.getRoot();
    }
}
