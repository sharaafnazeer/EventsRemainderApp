package com.example.mcs18440032.a1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.models.Event;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.List;

public class CheckDatabaseRunnable implements Runnable {

    EventEntity eventEntity;
    Context context;
    private static final String NOTIY = "NOTIY";

    public CheckDatabaseRunnable(Context context) {
        this.context = context;
        this.eventEntity = new EventEntity(context);
    }

    @Override
    public void run() {
        while (true) {
            Log.i("SERVICE", "Checking Database");
            try {
                createNotificationChannel();
                Thread.sleep(2000);
                checkAvailableEvents();
            } catch (InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAvailableEvents() throws ParseException {
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


            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIY)
                    .setSmallIcon(R.drawable.calendar_cell)
                    .setContentTitle("Alert")
                    .setContentText("Event title")
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
//                    .setContentIntent(fullScreenPendingIntent)
//                    .setTimeoutAfter(10000)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setAutoCancel(true);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that we define
//            notificationManager.notify((int) event.getId(), builder.build());

            Notification incomingCallNotification = builder.build();

            // Provide a unique integer for the "notificationId" of each notification.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService((int) event.getId(), incomingCallNotification);
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIY, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
