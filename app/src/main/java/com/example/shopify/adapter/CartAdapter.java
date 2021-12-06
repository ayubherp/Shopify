package com.example.shopify.adapter;

import static com.android.volley.Request.Method.GET;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.shopify.model.Item;
import com.example.shopify.model.ItemResponse;
import com.example.shopify.preferences.UserPreferences;
import com.example.shopify.ui.cart.CartActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolderCart>{
    private List<Cart> cartList;
    private List<Item> itemList;
    private Context context;
    private RequestQueue queue;
    private String token;
    private Cart cart;
    private Item item;

    public CartAdapter(List<Cart> cartList, List<Item> itemList, Context context){
        this.cartList = cartList;
        this.itemList = itemList;
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
        public void bindView(Cart cart, Item item)
        {
            binding.setCarts(cart);
            binding.setItems(item);
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
        holder.bindView(cartList.get(position), itemList.get(position));
        cart = cartList.get(position);
        item = itemList.get(position);

        holder.binding.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getAmount()==1)
                {
                    ((CartActivity) context).deleteCart(cart.getId());
                    notifyDataSetChanged();
                }
                else if(cart.getAmount()>1)
                {
                    int temp;
                    temp = cart.getAmount() - 1;
                    cart.setAmount(temp);
                    cart.setSubtotal(item.getPrice()*temp);
                    ((CartActivity) context).updateCart(
                            cart.getId(),cart.getId_user(),cart.getId_item(),
                            cart.getAmount(),cart.getSubtotal(),0);
                    notifyDataSetChanged();
                }
            }
        });
        holder.binding.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.getAmount()>=10)
                {
                    Toast.makeText(context, "Can't add more", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int temp;
                    temp = cart.getAmount() + 1;
                    cart.setAmount(temp);
                    cart.setSubtotal(item.getPrice()*temp);
                    ((CartActivity) context).updateCart(
                            cart.getId(),cart.getId_user(),cart.getId_item(),
                            cart.getAmount(),cart.getSubtotal(),0);
                    Toast.makeText(context, "Item added ", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, CartActivity.class);
                    context.startActivity(intent);
                    ((CartActivity)context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setAdapterToken(String token){
        this.token = token;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
        for(int i=0; i<this.cartList.size();i++)
        {
            StringRequest stringRequest = new StringRequest(GET,
                    ItemApi.GET_BY_ID_URL + this.cartList.get(i).getId_item(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                    Item item = itemResponse.getItemList().get(0);
                    itemList.add(item);
//                Glide.with(getBaseContext()).load(produk.getGambar()).apply(options).into(ivGambar);
//                etNama.setText(produk.getNama());
//                etHarga.setText(produk.getHarga().toString());
//                etDeskripsi.setText(produk.getDeskripsi());
//                etStok.setText(produk.getStok().toString());
//                Toast.makeText(AddEditActivity.this,
//                        produkResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    public List<Item> getItemList(){
        return itemList;
    }
    public List<Cart> getCartList(){
        return cartList;
    }

    public void payCartList(){
        Cart cart;
        for (int i = 0; i< cartList.size(); i++){
            cart = cartList.get(i);
            ((CartActivity) context)
                    .updateCart(
                            cart.getId(),cart.getId_user(),
                            cart.getId_item(), cart.getAmount(),
                            cart.getSubtotal(), 1);
        }
        cartList.clear();
        notifyDataSetChanged();
    }
    public void resetCartList(){
        Cart cart;
        for (int i = 0; i< cartList.size(); i++){
            cart = cartList.get(i);
            if (context instanceof CartActivity)
                ((CartActivity) context).deleteCart(cart.getId());
        }
        cartList.clear();
        notifyDataSetChanged();
    }

}
