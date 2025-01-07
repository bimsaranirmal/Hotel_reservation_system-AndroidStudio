package com.example.hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewDiscountsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDiscounts;
    private DiscountAdapter discountAdapter;
    private DB_Operations dbOperations;

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
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDiscountsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadDiscounts() {
        List<Discount> discountList = dbOperations.getAllDiscounts();
        discountAdapter = new DiscountAdapter(discountList);
        recyclerViewDiscounts.setAdapter(discountAdapter);
    }
}
