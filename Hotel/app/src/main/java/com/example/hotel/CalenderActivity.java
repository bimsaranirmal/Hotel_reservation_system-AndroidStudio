package com.example.hotel;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import Toolbar
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CalenderActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Set<String> bookedDates;
    private Set<String> reservedDates;
    private DB_Operations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calender);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendarView);
        bookedDates = new HashSet<>();
        reservedDates = new HashSet<>();

        dbOperations = new DB_Operations(this);

        String userEmail = getIntent().getStringExtra("USER_EMAIL"); // Retrieve the passed email
        if (userEmail != null) {
            loadBookedDates(userEmail);
            loadReservedDates(userEmail);
        } else {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
        }

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            checkDateStatus(selectedDate);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void checkDateStatus(String selectedDate) {
        if (bookedDates.contains(selectedDate)) {
            Toast.makeText(this, "This date is booked.", Toast.LENGTH_SHORT).show();
        } else if (reservedDates.contains(selectedDate)) {
            Toast.makeText(this, "This date is reserved for services.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Available date: " + selectedDate, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBookedDates(String email) {
        ArrayList<Reservation> bookings = dbOperations.getBookingsByEmail(email);
        for (Reservation booking : bookings) {
            String checkInDate = booking.getCheckInDate();
            String checkOutDate = booking.getCheckOutDate();

            // Add checkInDate
            bookedDates.add(checkInDate);

            // Add all dates between checkInDate and checkOutDate
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date startDate = sdf.parse(checkInDate);
                Date endDate = sdf.parse(checkOutDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                    bookedDates.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.DATE, 1);
                }
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadReservedDates(String email) {
        ArrayList<ServiceReservation> reservations = dbOperations.getServiceReservationsByEmail(email);
        for (ServiceReservation reservation : reservations) {
            reservedDates.add(reservation.getReservationDate());
        }
    }
}
