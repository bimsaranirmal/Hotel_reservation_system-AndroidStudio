package com.example.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Admin_Logins extends AppCompatActivity {

    private EditText txtUsername, txtPassword;
    private TextView lblAttempts;
    private Button btnLogin,btnCancel;

    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;
    private final int TIME_OUT_TIME = 30000;

    private final String HOTEL_ADMIN_USERNAME = "admin";
    private final String HOTEL_ADMIN_PASSWORD = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logins);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        lblAttempts = findViewById(R.id.lblAttempts);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void processLogin(View view) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username or Password cannot be Empty", Toast.LENGTH_SHORT).show();
        } else {
            if (HOTEL_ADMIN_USERNAME.equals(username) && HOTEL_ADMIN_PASSWORD.equals(password)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HotelAdminActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                loginAttempts++;
                if (loginAttempts >= MAX_ATTEMPTS) {
                    btnLogin.setEnabled(false);
                    Toast.makeText(this, "Too many Attempts. Try again in 30 seconds", Toast.LENGTH_SHORT).show();
                    lblAttempts.setText("Attempts: " + loginAttempts);

                    new Handler().postDelayed(() -> {
                        btnLogin.setEnabled(true);
                        loginAttempts = 0;
                    }, TIME_OUT_TIME);

                } else {
                    lblAttempts.setText("Attempts: " + loginAttempts);
                    Toast.makeText(this, "Login Failed. Attempts " + loginAttempts + " of " + MAX_ATTEMPTS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void cancel(View view){
        Intent intent = new Intent();
        startActivity(new Intent(this, Customer_Logins.class));
    }
}