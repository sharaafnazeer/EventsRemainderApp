package com.example.mcs18440032.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setTitleForActivity();

        Bundle bundle = getIntent().getExtras();
        int year = bundle.getInt(getString(R.string.year));
        int month = bundle.getInt(getString(R.string.month));
        int day = bundle.getInt(getString(R.string.day));
    }

    private void setTitleForActivity() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_event));
    }
}