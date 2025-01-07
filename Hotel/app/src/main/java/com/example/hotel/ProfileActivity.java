package com.example.hotel;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    private EditText editUsername, editMobile, editAddress, editEmail;
    private Button btnUpdate;
    private DB_Operations dbOperations;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editUsername = findViewById(R.id.editUsername);
        editMobile = findViewById(R.id.editMobile);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);
        btnUpdate = findViewById(R.id.btnUpdate);

        dbOperations = new DB_Operations(this);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail != null) {
            currentUser = dbOperations.getUserByEmail(userEmail);
            if (currentUser != null) {
                displayUserData();
            }
        }

        btnUpdate.setOnClickListener(v -> updateUserDetails());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void displayUserData() {
        if (currentUser != null) {
            Log.d("ProfileActivity", "Username: " + currentUser.getUsername());
            Log.d("ProfileActivity", "Mobile: " + currentUser.getMobile());
            Log.d("ProfileActivity", "Address: " + currentUser.getAddress());
            Log.d("ProfileActivity", "Email: " + currentUser.getEmail());

            editUsername.setText(currentUser.getUsername());
            editMobile.setText(String.valueOf(currentUser.getMobile()));
            editAddress.setText(currentUser.getAddress());
            editEmail.setText(currentUser.getEmail());
        }
    }

    private void updateUserDetails() {
        currentUser.setUsername(editUsername.getText().toString());
        currentUser.setMobile(Integer.parseInt(editMobile.getText().toString()));
        currentUser.setAddress(editAddress.getText().toString());

        boolean isUpdated = dbOperations.updateUsers(currentUser);
        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
