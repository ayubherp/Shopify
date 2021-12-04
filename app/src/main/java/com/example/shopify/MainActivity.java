package com.example.shopify;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopify.api.CartApi;
import com.example.shopify.api.ItemApi;
import com.example.shopify.databinding.ActivityMainBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.model.CartResponse;
import com.example.shopify.model.Item;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.announce.AnnounceFragment;
import com.example.shopify.ui.cart.CartActivity;
import com.example.shopify.ui.home.HomeFragment;
import com.example.shopify.ui.item.ItemFragment;
import com.example.shopify.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private User user;
    private UserPreferences userPreferences;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);
        userPreferences = new UserPreferences(getApplicationContext());
        user = userPreferences.getUserLogin();
        binding.navView.setSelectedItemId(R.id.navigation_home);
        binding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
                switch(menu.getItemId()){
                    case R.id.navigation_home:
                        changeFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_item_list:
                        changeFragment(new ItemFragment());
                        return true;
                    case R.id.navigation_announce:
                        changeFragment(new AnnounceFragment());
                        return true;
                    case R.id.navigation_profile:
                        changeFragment(new ProfileFragment());
                        return true;
                }
                return false;
            }
        });
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.putExtra("token",userPreferences.getUserLogin().getAccess_token());
                intent.putExtra("user_id",userPreferences.getUserLogin().getId());
                intent.putExtra("user_name",userPreferences.getUserLogin().getName());
                startActivity(intent);
            }
        });
        changeFragment(new HomeFragment());
    }
    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment_main,fragment)
                .commit();
    }

    public void addItemToCart(Item item){
        setLoading(true);
        Cart cart = new Cart(
                user.getId(),
                item.getId(), 1,
                item.getPrice(),
                0
        );
        StringRequest stringRequest = new StringRequest(POST, CartApi.ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        CartResponse cartResponse =
                                gson.fromJson(response, CartResponse.class);
                        Toast.makeText(MainActivity.this, cartResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        setLoading(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try{
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(MainActivity.this,
                            errors.getString("message"), Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
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
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(cart);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
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

