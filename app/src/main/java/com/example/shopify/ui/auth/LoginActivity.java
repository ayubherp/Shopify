package com.example.shopify.ui.auth;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.shopify.AdminActivity;
import com.example.shopify.MainActivity;
import com.example.shopify.R;
import com.example.shopify.api.UserApi;

import com.example.shopify.databinding.ActivityLoginBinding;
import com.example.shopify.databinding.ActivityMainBinding;
import com.example.shopify.model.AuthResponse;
import com.example.shopify.model.CartResponse;
import com.example.shopify.model.User;
import com.example.shopify.model.UserResponse;
import com.example.shopify.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private ActivityLoginBinding binding;
    private UserPreferences userPreferences;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userPreferences = new UserPreferences(LoginActivity.this);
        queue = Volley.newRequestQueue(this);

        /* Apps will check the login first from shared preferences */
        checkLogin();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        /* to clear the field just set text to "" */
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etEmail.setText("");
                binding.etPassword.setText("");
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    attemptLogin();
                }
            }
        });

    }


    private void attemptLogin(){
        setLoading(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserApi.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String token = jsonObject.getString("access_token");
                    if(success.equals("1"))
                    {
                        Gson gson = new Gson();
                        AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);
                        User user = authResponse.getUser();
                        if(user.getEmail_verified_at()!=null)
                        {
                            userPreferences.setUser(user.getId(), user.getName(),
                                    user.getEmail(), user.getPassword(), token);
                            checkLogin();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,
                                    "Must verify email first. Check your email box.", Toast.LENGTH_SHORT).show();

                        }
                        setLoading(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                checkLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(LoginActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params= new HashMap<String, String>();
                params.put(KEY_EMAIL, binding.etEmail.getText().toString());
                params.put(KEY_PASSWORD, binding.etPassword.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private boolean validateForm(){
        /* Check username & password is empty or not */
        if(binding.etEmail.getText().toString().trim().isEmpty() || binding.etPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(LoginActivity.this,"Username or Password still empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkLogin(){
        if(userPreferences.checkLogin() && userPreferences.getUserLogin().getId()==1){
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        }
        else if(userPreferences.checkLogin())
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
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