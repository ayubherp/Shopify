package com.example.shopify.ui.item;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopify.MainActivity;
import com.example.shopify.R;
import com.example.shopify.adapter.ItemAdapter;
import com.example.shopify.api.CartApi;
import com.example.shopify.api.ItemApi;
import com.example.shopify.databinding.FragmentItemBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.model.CartResponse;
import com.example.shopify.model.Item;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFragment extends Fragment {
    private RequestQueue queue;
    private ItemAdapter adapter;
    private FragmentItemBinding binding;
    public ItemFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_item, container, false);
        queue = Volley.newRequestQueue(this.getContext());
        binding.srItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllItem();
            }
        });
        binding.svItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        adapter = new ItemAdapter(new ArrayList<>(), this.getContext());
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvItem.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        } else {
            binding.rvItem.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        }
        binding.rvItem.setAdapter(adapter);
        getAllItem();
        return binding.getRoot();
    }

    private void getAllItem(){
        binding.srItem.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET,
                ItemApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                adapter.setProdukList(itemResponse.getItemList());
                adapter.getFilter().filter(binding.svItem.getQuery());

                Toast.makeText(getContext(),
                        itemResponse.getMessage(), Toast.LENGTH_SHORT).show();
                binding.srItem.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.srItem.setRefreshing(false);
                try{
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(getContext(),
                            errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accepts", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
