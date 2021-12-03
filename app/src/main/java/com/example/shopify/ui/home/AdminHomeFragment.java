package com.example.shopify.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.shopify.R;
import com.example.shopify.databinding.FragmentAdminHomeBinding;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.auth.LoginActivity;

public class AdminHomeFragment extends Fragment {
    private User user;
    private UserPreferences userPreferences;
    private FragmentAdminHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(
                inflater, R.layout.fragment_admin_home, container, false);
        userPreferences = new UserPreferences(getContext());
        user = userPreferences.getUserLogin();

        checkLogin();

        binding.tvWelcome.setText("Welcome to Shopify, "+user.getName());
        binding.tvWelcome2.setText("Let's control our data!");
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPreferences.logout();
                Toast.makeText(getContext(), "We're waiting for your back", Toast.LENGTH_SHORT).show();
                checkLogin();
            }
        });

        return binding.getRoot();
    }

    private void checkLogin(){
        /* this function will check if user login , akan memunculkan toast jika tidak redirect ke login activity */
        if(!userPreferences.checkLogin()){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }else {
            Toast.makeText(getContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
