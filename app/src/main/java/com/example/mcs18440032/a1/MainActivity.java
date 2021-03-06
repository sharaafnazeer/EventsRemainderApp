package com.example.mcs18440032.a1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mcs18440032.a1.adapters.CalendarAdapter;
import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.models.Event;
import com.example.mcs18440032.a1.services.NotificationService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public GregorianCalendar month, itemMonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker

    private EventEntity eventEntity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE},
                    PackageManager.PERMISSION_GRANTED);
        }

        eventEntity = new EventEntity(getApplicationContext());

        startBackgroundService(); // Start the background service

        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
//        month.setTimeZone(TimeZone.getDefault());
        itemMonth = (GregorianCalendar) month.clone();

        items = new ArrayList<>();
        adapter = new CalendarAdapter(this, month);

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = findViewById(R.id.title);
        title.setText(DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = findViewById(R.id.previous);

        previous.setOnClickListener(v -> {
            setPreviousMonth();
            refreshCalendar();
        });

        RelativeLayout next = findViewById(R.id.next);
        next.setOnClickListener(v -> {
            setNextMonth();
            refreshCalendar();

        });

        gridview.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedGridDate = CalendarAdapter.dayString
                    .get(position);
            Intent eventListIntent = new Intent(getApplicationContext(), EventListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.date), selectedGridDate);
            eventListIntent.putExtras(bundle);
            startActivity(eventListIntent);
            return true;
        });

        gridview.setOnItemClickListener((parent, v, position, id) -> {

            ((CalendarAdapter) parent.getAdapter()).setSelected(v);
            String selectedGridDate = CalendarAdapter.dayString
                    .get(position);
            String[] separatedTime = selectedGridDate.split("-");
            String gridValueString = separatedTime[2].replaceFirst("^0*",
                    "");// taking last part of date. ie; 2 from 2012-12-02.
            int gridValue = Integer.parseInt(gridValueString);
            // navigate to next or previous month on clicking offDays.
            if ((gridValue > 10) && (position < 8)) {
                setPreviousMonth();
                refreshCalendar();
            } else if ((gridValue < 7) && (position > 28)) {
                setNextMonth();
                refreshCalendar();
            } else {
                showToastScheduleActivity(selectedGridDate);
            }
            ((CalendarAdapter) parent.getAdapter()).setSelected(v);

        });

        try {
            checkAvailableEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        refreshCalendar();
        super.onResume();
    }

    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    /**
     * show Event Schedule Activity
     *
     * @param date //selected date
     */
    protected void showToastScheduleActivity(String date) {
        Intent launchEditorIntent = new Intent(this, ScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.date), date);
        launchEditorIntent.putExtras(bundle);
        startActivity(launchEditorIntent);
    }

    public void refreshCalendar() {
        TextView title = findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();
            // Print dates of the current week
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            for (int i = 0; i < 7; i++) {
                itemMonth.add(GregorianCalendar.DATE, 1);
                markEvents();
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };


    void markEvents() {
        try {
            List<Event> eventList = getAllEvents();

            for (Event event : eventList) {
                items.add(event.getDate());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<Event> getAllEvents() throws ParseException {
        return eventEntity.getAllByDate(null);
    }

    private void startBackgroundService() {
        startService(new Intent(MainActivity.this, NotificationService.class));
    }

    private void checkAvailableEvents() throws ParseException {
        DateTimeZone zoneUTC = DateTimeZone.UTC;
        DateTime now = DateTime.now();
        List<Event> eventList = eventEntity.getAllByDate(null);

        for (Event event : eventList) {

            System.out.println("TODAY's DATE === " + now.toString());
            System.out.println("EVENT DATE ====" + event.getDate());
            System.out.println("EVENT START === " + event.getStartTime());

            String eventDateTime = event.getDate() + " " + event.getStartTime();

            System.out.println("EVENT DATE & TIME ===  " + eventDateTime);

            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime eventActualTime = dateTimeFormatter.parseDateTime(eventDateTime);

            System.out.println("ACTUAL EVENT DATE & TIME ===  " + eventActualTime);

            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            LocalTime alertBefore = timeFormatter.parseLocalTime(event.getRemainder1());

            System.out.println("ALERT BEFORE ===  " + alertBefore);

            long alertLongTime = eventActualTime.getMillis() - alertBefore.getMillisOfDay();
            DateTime alertTime = new DateTime(alertLongTime);


            System.out.println("ACTUAL ALERT TIME ===  " + alertTime);

//            now.isAfter(alertLongTime);

            System.out.println("ALERT CHECK NOW > ALERT ===  " + now.isAfter(alertLongTime));
            System.out.println("ALERT CHECK NOW < ALERT ===  " + now.isBefore(alertLongTime));
            System.out.println("ALERT CHECK NOW = ALERT ===  " + now.isEqual(alertLongTime));
        }

        int i = 10;

    }
}