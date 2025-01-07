package com.example.hotel;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        videoView = findViewById(R.id.videoView);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userEmail != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView headerEmail = headerView.findViewById(R.id.header_email);
            headerEmail.setText(userEmail);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_rooms) {
                Toast.makeText(this, "Bookings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, BookRooms.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_services) {
                Toast.makeText(this, "Service book", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, BookServices.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_Bookings) {
                Toast.makeText(this, "Bookings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CustomerBookings.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_Reservations) {
                Toast.makeText(this, "Reservations", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CustomerReservations.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_discounts) {
                Toast.makeText(this, "Discounts", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CustomerDiscount.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_nearby_attractions) {
                Toast.makeText(this, "Attraction", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AttractionActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_offers) {
                Toast.makeText(this, "Exclusive Offers", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, OffersActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_events) {
                Toast.makeText(this, "Events", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EventsActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_contact) {
                Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ContactsActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_calender) {
                Toast.makeText(this, "Calendar", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CalenderActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_logout) {
                new AlertDialog.Builder(this)
                        .setTitle("Logout Confirmation")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, Customer_Logins.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            drawerLayout.closeDrawers();
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playVideo(View view){
        Uri uri = Uri.parse("android.resource://" +getPackageName()+ "/" + R.raw.aa);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });

        videoView.setOnCompletionListener(mp -> {
            videoView.seekTo(0);
            videoView.start();
        });

    }
}
