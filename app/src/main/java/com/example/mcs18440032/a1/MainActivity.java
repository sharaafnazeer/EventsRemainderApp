package com.example.mcs18440032.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declare calender view
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the calender view
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Intent scheduleActivity = new Intent(this, ScheduleActivity.class);

            // Passing calender values to the ScheduleActivity.class
            // Create a bundle object
            Bundle bundle = new Bundle();
            //Add your data from calender to bundle
            bundle.putInt(getString(R.string.year), year);
            bundle.putInt(getString(R.string.month), month + 1);
            bundle.putInt(getString(R.string.day), dayOfMonth);
            //Add the bundle to the intent
            scheduleActivity.putExtras(bundle);
            startActivity(scheduleActivity);
        });
    }
}