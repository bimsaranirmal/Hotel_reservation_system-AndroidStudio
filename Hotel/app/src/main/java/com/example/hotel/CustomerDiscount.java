package com.example.hotel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerDiscount extends AppCompatActivity {

    private RecyclerView recyclerViewDiscounts;
    private DiscountAdapter discountAdapter;
    private DB_Operations dbOperations;
    private static final String CHANNEL_ID = "discount_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_discounts);

        dbOperations = new DB_Operations(this);
        recyclerViewDiscounts = findViewById(R.id.recyclerViewDiscounts);
        recyclerViewDiscounts.setLayoutManager(new LinearLayoutManager(this));
        loadDiscounts();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonAdmin = findViewById(R.id.buttonBack);
        buttonAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDiscount.this, MainActivity.class);
            startActivity(intent);
        });

        // Trigger notifications for upcoming discounts
        notifyUpcomingDiscounts();
    }

    private void loadDiscounts() {
        List<Discount> discountList = dbOperations.getAllDiscounts();
        discountAdapter = new DiscountAdapter(discountList);
        recyclerViewDiscounts.setAdapter(discountAdapter);
    }

    private void notifyUpcomingDiscounts() {
        List<Discount> discountList = dbOperations.getAllDiscounts();
        for (Discount discount : discountList) {
            if (isDiscountStartingSoon(discount)) {
                sendDiscountNotification(discount);
            }
        }
    }

    private boolean isDiscountStartingSoon(Discount discount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = dateFormat.parse(discount.getStartDate());
            Calendar currentDate = Calendar.getInstance();
            Calendar discountDate = Calendar.getInstance();
            discountDate.setTime(startDate);

            long timeDifference = discountDate.getTimeInMillis() - currentDate.getTimeInMillis();
            return timeDifference <= 24 * 60 * 60 * 1000; // Check if within 24 hours
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendDiscountNotification(Discount discount) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Upcoming Discount Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifies users of upcoming discounts");
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, CustomerDiscount.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        android.app.Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new android.app.Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Upcoming Discount Alert")
                    .setContentText("Discount " + discount.getDiscountName() + " starts on " + discount.getStartDate())
                    .setSmallIcon(R.drawable.stars) // Use an appropriate icon
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
