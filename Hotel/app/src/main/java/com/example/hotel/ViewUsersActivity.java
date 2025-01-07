package com.example.hotel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;
    private DB_Operations dbOperations;
    private Button btnBack;
    private EditText editTextSearchEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextSearchEmail = findViewById(R.id.editTextSearchEmail);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        userList = new ArrayList<>();
        dbOperations = new DB_Operations(this);

        userList = dbOperations.getAllUsers();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);

        buttonSearch.setOnClickListener(v -> searchByEmail());
    }

    private void searchByEmail() {
        String email = editTextSearchEmail.getText().toString().trim();
        if (!email.isEmpty()) {
            List<User> filteredList = new ArrayList<>();
            for (User user : userList) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    filteredList.add(user);
                }
            }
            if (filteredList.isEmpty()) {
                userAdapter.updateUserList(userList);
            } else {
                userAdapter.updateUserList(filteredList);
            }
        } else {
            userAdapter.updateUserList(userList);
        }
    }


    public void back(View view) {
        Intent intent = new Intent(ViewUsersActivity.this, HotelAdminActivity.class);
        startActivity(intent);
        finish();
    }
}
