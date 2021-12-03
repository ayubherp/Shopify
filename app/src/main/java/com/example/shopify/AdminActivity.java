package com.example.shopify;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.example.shopify.adapter.UserAdapter;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.model.User;
import com.example.shopify.model.UserResponse;
import com.example.shopify.ui.home.AdminHomeFragment;
import com.example.shopify.ui.home.HomeFragment;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopify.adapter.ItemAdapter;
import com.example.shopify.api.ItemApi;
import com.example.shopify.api.UserApi;
import com.example.shopify.databinding.ActivityAdminBinding;
import com.example.shopify.ui.item.AddEditActivity;
import com.example.shopify.ui.item.AdminItemFragment;
import com.example.shopify.ui.user.AdminUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mapbox.api.directions.v5.models.Admin;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    public static final int LAUNCH_ADD_ACTIVITY = 123;

    private ActivityAdminBinding binding;
    private ItemAdapter itemAdapter;
    private UserAdapter userAdapter;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);

        binding.navView.setSelectedItemId(R.id.navigation_admin_home);
        binding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                switch (menu.getItemId()) {
                    case R.id.navigation_admin_item:
                        changeFragment(new AdminItemFragment());
                        return true;
                    case R.id.navigation_admin_user:
                        changeFragment(new AdminUserFragment());
                        return true;
                    case R.id.navigation_admin_home:
                        changeFragment(new AdminHomeFragment());
                        return true;
                }
                return false;
            }
        });
        changeFragment(new AdminHomeFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK)
            getAllItem();
    }

    private void getAllItem() {
        StringRequest stringRequest = new StringRequest(GET, ItemApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                itemAdapter.setProdukList(itemResponse.getItemList());
                Toast.makeText(AdminActivity.this, itemResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };
        queue.add(stringRequest);
    }

    public void deleteItem(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(DELETE, ItemApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                setLoading(false);
                Toast.makeText(AdminActivity.this, itemResponse.getMessage(), Toast.LENGTH_SHORT).show();
                getAllItem();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };
        queue.add(stringRequest);
    }


    public void deleteUser(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(DELETE, UserApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(response, UserResponse.class);
                setLoading(false);
                Toast.makeText(AdminActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                getAllItem();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment_admin,fragment)
                .commit();
    }

    private void setLoading(boolean isLoading) {
        LinearLayout layoutLoading = findViewById(R.id.loading_layout);
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }
}