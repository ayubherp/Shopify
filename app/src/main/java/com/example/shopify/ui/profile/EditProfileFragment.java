package com.example.shopify.ui.profile;

import static com.android.volley.Request.Method.PUT;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.shopify.R;
import com.example.shopify.api.UserApi;
import com.example.shopify.databinding.FragmentEditProfileBinding;
import com.example.shopify.model.User;
import com.example.shopify.model.UserResponse;
import com.example.shopify.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {
    private RequestQueue queue;
    private Handler handler;
    private UserPreferences userPreferences;
    private User user;
    private FragmentEditProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(
                inflater, R.layout.fragment_edit_profile, container, false);
        userPreferences = new UserPreferences(getContext());
        user = userPreferences.getUserLogin();

        try {
            if (user.getName() != null) {
                binding.etNama.setText(user.getName());
            } else {
                binding.etNama.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (user.getEmail() != null) {
                binding.etEmail.setText(user.getEmail());
            } else {
                binding.etEmail.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(String.valueOf(binding.etNama.getText()));
                user.setEmail(String.valueOf(binding.etEmail.getText()));
                userPreferences.setUser(user.getId(),user.getName(), user.getEmail());
                updateProfile(user.getId());
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_profile, new ProfileFragment()).commit();
            }
        });
    }

    private void updateProfile(long id) {
        User user = new User(id, this.user.getName(), this.user.getEmail());

        StringRequest stringRequest = new StringRequest(PUT, UserApi.UPDATE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(response, UserResponse.class);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                } catch (Exception e) {

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(user);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
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
