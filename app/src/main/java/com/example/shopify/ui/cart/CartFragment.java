package com.example.shopify.ui.cart;

import static com.example.shopify.model.MyApplication.CHANNEL_1_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopify.R;
import com.example.shopify.adapter.CartAdapter;
import com.example.shopify.database.DatabaseClient;
import com.example.shopify.databinding.FragmentCartItemBinding;
import com.example.shopify.model.Cart;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;

import java.util.List;

public class CartFragment extends Fragment {
    private User user;
    private UserPreferences userPreferences;
    private List<Cart> cartList;
    private CartAdapter adapter;
    private FragmentCartItemBinding binding;
    private NotificationManagerCompat notificationManager;
    private Notification notification;

    public CartFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart, container, false);
        userPreferences = new UserPreferences(getContext().getApplicationContext());
        user = userPreferences.getUserLogin();
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationManager = NotificationManagerCompat.from(getContext());
        getCartItems();
        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalPrice(cartList)!=0)
                {
                    //send notification
                    Intent activityIntent = new Intent(getContext(), CartActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, activityIntent, 0);

                    Bitmap myIcon = BitmapFactory.decodeResource(getResources(),R.drawable.shopify_logo);
                    notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_baseline_shopping_bag_24)
                            .setLargeIcon(myIcon)
                            .setContentTitle("Shopify Cart Mail")
                            .setContentText("~See detail~")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("We charged your wallet balance : Rp. " +String.format("%.0f",totalPrice(cartList))
                                            +". Item will send to your address : "+user.getAlamat() )
                                    .setBigContentTitle("Payment Confirmed")
                                    .setSummaryText("Summary"))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setColor(Color.RED)
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(true)
                            .build();

                    notificationManager.notify(3, notification);

                    //remove from cart database
                    adapter.removeCartItems();
                    binding.txtTotal.setText(String.format("%.0f",totalPrice(cartList)));
                    Toast.makeText(view.getRootView().getContext(), "Payment success", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(view.getRootView().getContext(), "No items to pay", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeCartItems();
                binding.txtTotal.setText(String.format("%.0f",totalPrice(cartList)));
                Toast.makeText(v.getRootView().getContext(), "Reset Cart success", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getCartItems() {
        class GetCartItems extends AsyncTask<Void, Void, List<Cart>> {

            @Override
            protected List<Cart> doInBackground(Void... voids) {
                List<Cart> itemList = DatabaseClient.getInstance(getContext())
                        .getDatabase()
                        .listCartItemDao()
                        .getCartsByUserId(userPreferences.getUserLogin().getId());
                return itemList;
            }

            @Override
            protected void onPostExecute(List<Cart> cartItems) {
                super.onPostExecute(cartItems);
                adapter = new CartAdapter(cartItems, getContext());
                binding.rvCartItems.setAdapter(adapter);
                cartList = adapter.getItems();
                binding.txtTotal.setText(String.format("%.0f",totalPrice(cartList)));
            }
        }
        GetCartItems getCartItems = new GetCartItems();
        getCartItems.execute();
    }

    public double totalPrice(List<Cart> items){
        double totalPrice = 0;
        for (int i=0; i<items.size();i++){
            totalPrice += items.get(i).getTotal();
        }
        return totalPrice;
    }
}
