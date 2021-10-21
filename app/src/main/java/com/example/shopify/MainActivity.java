package com.example.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.shopify.databinding.ActivityMainBinding;
import com.example.shopify.ui.cart.CartActivity;
import com.example.shopify.ui.home.HomeFragment;
import com.example.shopify.ui.item.ItemFragment;
import com.example.shopify.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                startActivity(intent);
            }
        });
        changeFragment(new HomeFragment());
    }
    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment_main,fragment)
                .commit();
    }
}

