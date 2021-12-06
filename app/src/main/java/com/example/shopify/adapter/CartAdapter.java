package com.example.shopify.adapter;

import static com.android.volley.Request.Method.GET;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopify.R;
import com.example.shopify.api.ItemApi;
import com.example.shopify.databinding.CartViewBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.model.CartItem;
import com.example.shopify.model.Item;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.cart.CartActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolderCart>{
    private List<CartItem> cartItemList;
    private Context context;
    private RequestQueue queue;
    private String token, user_name;
    private Long user_id;
    private Cart cart;
    private Item item;

    public CartAdapter(List<CartItem> cartItemList, Context context){
        this.cartItemList = cartItemList;
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }


    public class viewHolderCart extends RecyclerView.ViewHolder {
        CartViewBinding binding;
        public viewHolderCart(@NonNull CartViewBinding binding){
            super(binding.getRoot());
            this.binding = binding;

            this.binding.cartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TEST", "onClick: " +getAdapterPosition());
                }
            });
        }
        public void bindView(CartItem cart)
        {
            binding.setCarts(cart);
        }
    }

    @NonNull
    @Override
    public viewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CartViewBinding bindingItem = CartViewBinding.inflate(layoutInflater, parent, false);
        return new viewHolderCart(bindingItem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderCart holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        holder.binding.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getAmount()==1)
                {
                    ((CartActivity) context).deleteCart(cartItem.getId_cart());
                    Intent intent = new Intent(context, CartActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("user_name",user_name);
                    context.startActivity(intent);
                    ((CartActivity)context).finish();
                }
                else if(cartItem.getAmount()>1)
                {
                    int temp;
                    temp = cartItem.getAmount() - 1;
                    cartItem.setAmount(temp);
                    cartItem.setSubtotal(cartItem.getPrice()*temp);
                    ((CartActivity) context).updateCart(
                            cartItem.getId_cart(),cartItem.getId_user(),cartItem.getId_item(),
                            cartItem.getAmount(),cartItem.getSubtotal(),0);
                    Intent intent = new Intent(context, CartActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("user_name",user_name);
                    context.startActivity(intent);
                    ((CartActivity)context).finish();
                }
            }
        });
        holder.binding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItem.getAmount()>=10)
                {
                    Toast.makeText(context, "Can't add more", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int temp;
                    temp = cartItem.getAmount() + 1;
                    cartItem.setAmount(temp);
                    cartItem.setSubtotal(cartItem.getPrice()*temp);
                    ((CartActivity) context).updateCart(
                            cartItem.getId_cart(),cartItem.getId_user(),cartItem.getId_item(),
                            cartItem.getAmount(),cartItem.getSubtotal(),0);
                    Toast.makeText(context, "Item added ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, CartActivity.class);
                    intent.putExtra("token",token);
                    intent.putExtra("user_id",user_id);
                    intent.putExtra("user_name",user_name);
                    context.startActivity(intent);
                    ((CartActivity)context).finish();
                }
            }
        });
        holder.binding.tvBrand.setText(cartItem.getName());
        holder.binding.tvType.setText(cartItem.getType());
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.binding.tvSubtotal.setText(rupiah.format(cartItem.getSubtotal()));
        holder.binding.tvPrice.setText(rupiah.format(cartItem.getPrice()));
        holder.binding.tvAmount.setText(String.valueOf(cartItem.getAmount()));
        holder.binding.tvAmount2.setText(String.valueOf(cartItem.getAmount()));
        byte[] imageByteArray = Base64.decode(cartItem.getImage(),Base64.DEFAULT);
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imageByteArray)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.imgCart);
        holder.bindView(cartItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void setUser(String token, Long user_id, String user_name){
        this.token = token;
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public void setCartItemList(List<Cart> cartList) {
        for(Cart cart : cartList)
        {
            if(cart.getId_user().equals(user_id) && cart.getStatus()==0){
                StringRequest stringRequest = new StringRequest(GET,
                        ItemApi.GET_BY_ID_URL + cart.getId_item(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                        Item item = itemResponse.getItem();
                        CartItem cartItem = new CartItem(cart.getId_item(),cart.getId_user(),
                                cart.getId_item(), item.getName(),
                                item.getPrice(),item.getType(),item.getImage(),
                                cart.getAmount(), cart.getSubtotal(),cart.getStatus());
                        cartItemList.add(cartItem);
                        Log.d("tes","test");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data,
                                    StandardCharsets.UTF_8);
                            JSONObject errors = new JSONObject(responseBody);
                            Toast.makeText(context, errors.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String auth = "Bearer " + token;
                        headers.put("Authorization", auth);
                        headers.put("Accept", "application/json");
                        return headers;
                    }
                };
                queue.add(stringRequest);
            }
        }
        notifyDataSetChanged();
    }

    public void setCartItemList2(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public List<CartItem> getCartItemList(){
        return cartItemList;
    }
    public void payCartList(){
        CartItem cart;
        for (int i = 0; i< cartItemList.size(); i++){
            cart = cartItemList.get(i);
            ((CartActivity) context)
                    .updateCart(
                            cart.getId_cart(),cart.getId_user(),
                            cart.getId_item(), cart.getAmount(),
                            cart.getSubtotal(), 1);
        }
        cartItemList.clear();
        notifyDataSetChanged();
    }
    public void resetCartList(){
        CartItem cart;
        for (int i = 0; i< cartItemList.size(); i++){
            cart = cartItemList.get(i);
            if (context instanceof CartActivity)
                ((CartActivity) context).deleteCart(cart.getId_cart());
        }
        cartItemList.clear();
        notifyDataSetChanged();
    }

}
