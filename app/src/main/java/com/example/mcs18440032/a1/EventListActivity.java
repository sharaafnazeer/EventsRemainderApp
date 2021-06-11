package com.example.mcs18440032.a1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mcs18440032.a1.adapters.EventListAdapter;
import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.models.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListActivity extends AppCompatActivity {

    EventEntity eventEntity;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList;
    private String eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        setTitleForActivity();

        initializeItems();
        try {
            initializeDataEntity();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            initializeDataEntity();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void initializeDataEntity() throws ParseException {
        eventEntity = new EventEntity(getApplicationContext());
        eventList = eventEntity.getAllByDate(eventDate);
        eventListAdapter.setEventList(eventList);
        eventListAdapter.notifyDataSetChanged();
    }

    private void setTitleForActivity() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.event_list));
    }

    private void initializeItems() {
        // Get bundle values form previous activity
        Bundle bundle = getIntent().getExtras();
        eventDate = bundle.getString(getString(R.string.date));

        RecyclerView rvEvents = findViewById(R.id.rvEvents);
        eventList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(eventList);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventListAdapter);
    }


}