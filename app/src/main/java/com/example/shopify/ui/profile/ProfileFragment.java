package com.example.shopify.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopify.MainActivity;
import com.example.shopify.R;
import com.example.shopify.databinding.FragmentProfileBinding;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.auth.LoginActivity;
import com.example.shopify.ui.mapbox.GeolocationActivity;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {
    private User user;
    private UserPreferences userPreferences;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);

        userPreferences = new UserPreferences(getContext());
        user = userPreferences.getUserLogin();
        checkLogin();

        binding.setUser(user);
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new EditProfileFragment());
            }
        });
        binding.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivity = new Intent(getContext(), GeolocationActivity.class);
                startActivity(mapActivity);
            }
        });

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
        /* check if user login */
        if(!userPreferences.checkLogin()){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
