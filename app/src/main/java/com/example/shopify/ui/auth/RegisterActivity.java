package com.example.shopify.ui.auth;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopify.R;
import com.example.shopify.api.UserApi;
import com.example.shopify.databinding.ActivityLoginBinding;
import com.example.shopify.databinding.ActivityRegisterBinding;
import com.example.shopify.model.User;
import com.example.shopify.model.UserResponse;
import com.example.shopify.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    register();
                }
            }
        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearField();
            }
        });
    }

    private void register(){
        setLoading(true);
        if (validateForm()) {
            StringRequest stringRequest = new StringRequest(POST, UserApi.REGISTER_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        Log.d("test","test1");
                        if(success.equals("1")){
                            Log.d("test","test2");
                            Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                            AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Register Success")
                                    .setMessage("Email verification has been sent to your email address. You must verify to access Shopify")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            toLoginActivity();
                                        }
                                    }).setCancelable(false).create();
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Register error!" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                        setLoading(false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(RegisterActivity.this, "Register Error!" +error.toString(),
                                Toast.LENGTH_SHORT).show();
                        setLoading(false);
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params= new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    params.put("Accept", "application/json");
                    params.put("email",binding.etEmail.getText().toString());
                    params.put("name",binding.etName.getText().toString());
                    params.put("password",binding.etPassword.getText().toString());
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }

    private void clearField(){
        binding.etName.setText("");
        binding.etEmail.setText("");
        binding.etPassword.setText("");
    }


    private boolean validateForm(){
        /* Check username & password is empty or not */
        if(binding.etEmail.getText().toString().trim().isEmpty()
                || binding.etPassword.getText().toString().trim().isEmpty() || binding.etName.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this,"Fill all the form please..",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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