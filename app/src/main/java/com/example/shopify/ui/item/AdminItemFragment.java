package com.example.shopify.ui.item;

import static com.android.volley.Request.Method.GET;
import static com.example.shopify.preferences.UserPreferences.KEY_EMAIL;
import static com.example.shopify.preferences.UserPreferences.KEY_TOKEN;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopify.AdminActivity;
import com.example.shopify.R;
import com.example.shopify.adapter.ItemAdapter;
import com.example.shopify.adapter.ItemAdminAdapter;
import com.example.shopify.api.ItemApi;
import com.example.shopify.databinding.FragmentAdminItemBinding;
import com.example.shopify.databinding.FragmentItemBinding;
import com.example.shopify.model.Item;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.preferences.UserPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminItemFragment extends Fragment {
    private RequestQueue queue;
    private ItemAdminAdapter adapter;
    private FragmentAdminItemBinding binding;
    private UserPreferences userPreferences;

    public AdminItemFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_admin_item, container, false);
        queue = Volley.newRequestQueue(this.getContext());
        userPreferences = new UserPreferences(getContext());
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
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEditActivity.class);
                intent.putExtra("token",userPreferences.getUserLogin().getAccess_token());
                startActivity(intent);
            }
        });
        adapter = new ItemAdminAdapter(new ArrayList<>(), this.getContext());
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvItem.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        } else {
            binding.rvItem.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        }
        binding.rvItem.setAdapter(adapter);
        binding.srItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllItem();
            }
        });
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
                HashMap<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + userPreferences.getUserLogin().getAccess_token();
                headers.put("Authorization", auth);
                headers.put("Accept", "application/json");
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
