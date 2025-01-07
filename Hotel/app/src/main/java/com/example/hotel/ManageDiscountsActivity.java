package com.example.hotel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class ManageDiscountsActivity extends AppCompatActivity {
    private EditText txtDiscountID, txtDiscountName, txtDescription, txtDiscountPercentage, txtStartDate, txtEndDate, txtApplicableRoomType;
    private DB_Operations dbOperations;
    Button btnClearFields;
    private Button btnStartDate, btnEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_discounts);

        dbOperations = new DB_Operations(this);

        txtDiscountID = findViewById(R.id.textDiscountID);
        txtDiscountName = findViewById(R.id.textDiscountName);
        txtDescription = findViewById(R.id.textDescription);
        txtDiscountPercentage = findViewById(R.id.textDiscountPercentage);
        txtStartDate = findViewById(R.id.textStartDate);
        txtEndDate = findViewById(R.id.textEndDate);
        txtApplicableRoomType = findViewById(R.id.textApplicableRoomType);
        btnClearFields = findViewById(R.id.btnClearFields);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        btnClearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        // Set up date pickers
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    private void showDatePickerDialog(boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        if (isStartDate) {
                            Calendar currentDate = Calendar.getInstance();
                            if (selectedCalendar.before(currentDate)) {
                                Toast.makeText(ManageDiscountsActivity.this, "Start date must be in the future.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            txtStartDate.setText(selectedDate);
                        } else {
                            Calendar startDate = Calendar.getInstance();
                            String startDateString = txtStartDate.getText().toString();
                            if (!startDateString.isEmpty()) {
                                String[] parts = startDateString.split("-");
                                startDate.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
                                if (selectedCalendar.before(startDate)) {
                                    Toast.makeText(ManageDiscountsActivity.this, "End date must be after the start date.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            txtEndDate.setText(selectedDate);
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    private boolean validateFields() {
        if (txtDiscountID.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Discount ID cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDiscountName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Discount Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDiscountPercentage.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Discount Percentage cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtStartDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Start Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtEndDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "End Date cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtApplicableRoomType.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Applicable Room Type cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void addDiscount(View view) {
        if (!validateFields()) {
            return;
        }
        try {
            int discountID = Integer.parseInt(txtDiscountID.getText().toString());
            String discountName = txtDiscountName.getText().toString();
            String description = txtDescription.getText().toString();
            double discountPercentage = Double.parseDouble(txtDiscountPercentage.getText().toString());
            String startDate = txtStartDate.getText().toString();
            String endDate = txtEndDate.getText().toString();
            String applicableRoomType = txtApplicableRoomType.getText().toString();

            Discount discount = new Discount(discountID, discountName, description, discountPercentage, startDate, endDate, applicableRoomType);
            dbOperations.addDiscount(discount);
            Toast.makeText(this, "Discount added successfully!", Toast.LENGTH_SHORT).show();

            sendSMSToAllUsers(discountName, discountPercentage, startDate, endDate);

            clearFields();
        } catch (Exception e) {
            Toast.makeText(this, "Error adding discount: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendSMSToAllUsers(String discountName, double discountPercentage, String startDate, String endDate) {
        ArrayList<User> userList = dbOperations.getAllUsers();

        String message = String.format("New Discount Available at LuxeVista Resort: %s (%.2f%% off) from %s to %s.",
                discountName, discountPercentage, startDate, endDate);

        for (User user : userList) {
            String phoneNumber = String.valueOf(user.getMobile()); // Assuming Mobile is an int
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }

        Toast.makeText(this, "SMS notifications sent to all users.", Toast.LENGTH_SHORT).show();
    }

    public void searchDiscount(View view) {
        try {
            int discountID = Integer.parseInt(txtDiscountID.getText().toString());
            Discount discount = dbOperations.getDiscountById(discountID);
            if (discount != null) {
                txtDiscountName.setText(discount.getDiscountName());
                txtDescription.setText(discount.getDescription());
                txtDiscountPercentage.setText(String.valueOf(discount.getDiscountPercentage()));
                txtStartDate.setText(discount.getStartDate());
                txtEndDate.setText(discount.getEndDate());
                txtApplicableRoomType.setText(discount.getApplicableRoomType());
            } else {
                Toast.makeText(this, "Discount not found!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid Discount ID.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDiscount(View view) {
        if (!validateFields()) {
            return;
        }
        try {
            int discountID = Integer.parseInt(txtDiscountID.getText().toString());
            String discountName = txtDiscountName.getText().toString();
            String description = txtDescription.getText().toString();
            double discountPercentage = Double.parseDouble(txtDiscountPercentage.getText().toString());
            String startDate = txtStartDate.getText().toString();
            String endDate = txtEndDate.getText().toString();
            String applicableRoomType = txtApplicableRoomType.getText().toString();

            Discount discount = new Discount(discountID, discountName, description, discountPercentage, startDate, endDate, applicableRoomType);
            dbOperations.updateDiscount(discount);
            Toast.makeText(this, "Discount updated successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } catch (Exception e) {
            Toast.makeText(this, "Error updating discount: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void deleteDiscount(View view) {
        if (txtDiscountID.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a Discount ID to delete.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int discountID = Integer.parseInt(txtDiscountID.getText().toString());
            dbOperations.deleteDiscount(discountID);
            Toast.makeText(this, "Discount deleted successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } catch (Exception e) {
            Toast.makeText(this, "Error deleting discount: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtDiscountID.setText("");
        txtDiscountName.setText("");
        txtDescription.setText("");
        txtDiscountPercentage.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtApplicableRoomType.setText("");
    }
}
