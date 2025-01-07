package com.example.hotel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HotelAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_admin_activities);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.client_6009897);
        }

        navigationView.setNavigationItemSelectedListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_manage_users) {
            Toast.makeText(this, "Manage Users clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ManageUsersActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else if (item.getItemId() == R.id.nav_view_users) {
            Toast.makeText(this, "View Users clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewUsersActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_manage_rooms) {
            Toast.makeText(this, "Manage Rooms clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ManageRoomsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_view_rooms) {
            Toast.makeText(this, "View Rooms clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminViewRoomsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_manage_services) {
            Toast.makeText(this, "Manage Services clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ManageServicesActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_view_services) {
            Toast.makeText(this, "View Services clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminViewServicesActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_manage_discount) {
            Toast.makeText(this, "Discounts clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ManageDiscountsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_view_discounts) {
            Toast.makeText(this, "View discounts clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewDiscountsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_manage_bookings) {
            Toast.makeText(this, "Manage Bookings clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewBookingsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_manage_reservations) {
            Toast.makeText(this, "Manage Reservations clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewServiceReservationActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else if (item.getItemId() == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout Confirmation")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Admin_Logins.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}