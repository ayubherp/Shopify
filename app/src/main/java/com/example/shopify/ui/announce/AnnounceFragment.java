package com.example.shopify.ui.announce;

import static com.example.shopify.model.MyApplication.CHANNEL_1_ID;
import static com.example.shopify.model.MyApplication.CHANNEL_2_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.shopify.MainActivity;
import com.example.shopify.R;
import com.example.shopify.databinding.FragmentAnnounceBinding;
import com.example.shopify.model.NotificationReceiver;
import com.example.shopify.model.User;
import com.example.shopify.preferences.UserPreferences;

public class AnnounceFragment extends Fragment {
    private User user;
    private UserPreferences userPreferences;
    private FragmentAnnounceBinding binding;
    private Notification notification;
    private NotificationManagerCompat notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_announce, container, false);
        notificationManager = NotificationManagerCompat.from(getContext());
        userPreferences = new UserPreferences(getContext());
        user = userPreferences.getUserLogin();

        binding.btnGreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityIntent = new Intent(getContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, activityIntent, 0);

                Bitmap myIcon = BitmapFactory.decodeResource(getResources(),R.drawable.shopify_logo);
                notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_shopping_bag_24)
                        .setContentTitle("Shopify here!")
                        .setContentText("~See greetings~")
                        .setLargeIcon(myIcon)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setBigContentTitle("Greetings")
                                .bigText("Welcome "+user.getName() +", glad to see you here. Let's shop with Shopify!")
                                .setSummaryText("Summary"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setColor(Color.BLACK)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .build();

                notificationManager.notify(0, notification);
            }
        });

        binding.btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = new Intent(getContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, activityIntent, 0);

                Bitmap myIcon = BitmapFactory.decodeResource(getResources(),R.drawable.shopify_logo);
                Bitmap myPic = BitmapFactory.decodeResource(getResources(),R.drawable.retro_question);
                notification = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                        .setSmallIcon(R.drawable.ic_baseline_shopping_bag_24)
                        .setLargeIcon(myIcon)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(myPic)
                                .bigLargeIcon(myIcon)
                                .setBigContentTitle("Shopify Hi-Quality Product")
                                .setSummaryText("We will release new items. Currently under development, let's go!"))
                        .setContentTitle("New Items")
                        .setContentText("~Releases guide~")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setColor(Color.RED)
                        .setContentIntent(contentIntent)
                        .setOnlyAlertOnce(true)
                        .build();

                notificationManager.notify(1, notification);
            }
        });
        binding.btnDevInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification = new NotificationCompat.Builder(getContext().getApplicationContext(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_groups_24)
                        .setContentTitle("Developer")
                        .setContentText("The Man Behind")
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Ayub Her Pracoyo - 190710243")
                                .addLine("Marvellius Arthur P.S. - 190710125")
                                .addLine("Edward Sebastian Eka S. - 190710501")
                                .setSummaryText("UAJY"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setGroup("sample_group")
                        .setColor(Color.BLUE)
                        .build();

                notificationManager.notify(2, notification);
            }
        });
        return binding.getRoot();
    }
}
