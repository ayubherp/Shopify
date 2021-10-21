package com.example.shopify.ui.cart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopify.R;
import com.example.shopify.adapter.ListCartItemAdapter;
import com.example.shopify.database.DatabaseClient;
import com.example.shopify.databinding.FragmentCartItemBinding;
import com.example.shopify.model.CartItem;
import com.example.shopify.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class CartItemFragment extends Fragment {
    private UserPreferences userPreferences;
    private List<CartItem> cartItemList;
    private ListCartItemAdapter adapter;
    private FragmentCartItemBinding binding;

    public CartItemFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart_item, container, false);
        userPreferences = new UserPreferences(getContext().getApplicationContext());
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getRootView().getContext(), "Payment success", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getRootView().getContext(), "Reset success", Toast.LENGTH_SHORT).show();
            }
        });
        getCartItems();
        cartItemList = new ArrayList<>();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getCartItems() {
        class GetCartItems extends AsyncTask<Void, Void, List<CartItem>> {

            @Override
            protected List<CartItem> doInBackground(Void... voids) {
                List<CartItem> itemList = DatabaseClient.getInstance(getContext())
                        .getDatabase()
                        .listCartItemDao()
                        .getCartsByUserId(userPreferences.getUserLogin().getId());
                return itemList;
            }

            @Override
            protected void onPostExecute(List<CartItem> cartItems) {
                super.onPostExecute(cartItems);
                adapter = new ListCartItemAdapter(cartItems, getContext());
                binding.rvCartItems.setAdapter(adapter);
            }
        }
        GetCartItems getCartItems = new GetCartItems();
        getCartItems.execute();
    }
}
