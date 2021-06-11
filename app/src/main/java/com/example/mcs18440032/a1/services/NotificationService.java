package com.example.mcs18440032.a1.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mcs18440032.a1.MainActivity;
import com.example.mcs18440032.a1.R;
import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.helpers.Helper;
import com.example.mcs18440032.a1.models.Event;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private EventEntity eventEntity;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
    private Timer timer;

    @Override
    public void onCreate() {
        eventEntity = new EventEntity(getApplication().getApplicationContext());
        timer = new Timer();
        Log.i("SERVICE", "Service Created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkAvailableEvents();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("SERVICE", "Service Stopped");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkAvailableEvents() throws ParseException {
        DateTime now = DateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime actualCurrentTime = dateTimeFormatter.parseDateTime(now.toString(dateTimeFormatter));

        List<Event> eventList = eventEntity.getAllByDate(null);
        for (Event event : eventList) {
            String eventDateTime = event.getDate() + " " + event.getStartTime();
            DateTime eventActualTime = dateTimeFormatter.parseDateTime(eventDateTime);
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            LocalTime alertBefore = timeFormatter.parseLocalTime(event.getRemainder1());
            long eventLongTime = eventActualTime.getMillis();
            long alertLongTime = eventLongTime - alertBefore.getMillisOfDay();
            System.out.println("ALERT CHECK NOW > ALERT ===  " + actualCurrentTime.isAfter(alertLongTime));
            System.out.println("ALERT CHECK NOW < ALERT ===  " + actualCurrentTime.isBefore(alertLongTime));
            System.out.println("ALERT CHECK NOW = ALERT ===  " + actualCurrentTime.isEqual(alertLongTime));

            if (eventActualTime.isAfter(actualCurrentTime.getMillis())) {
                if (actualCurrentTime.isEqual(alertLongTime) && event.getIsRemainder() != 1) {
                    createNotifications(event.getId(), event.getEventName(), event.getStartTime());
                }
            }
        }
    }

    private void createNotifications(long id, String title, String startTime) {
        // Create an explicit intent for an Activity in the app
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Event Remainder")
                .setContentText(title + " at " + startTime)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.argb(255, 234, 146, 21), 1000, 10000)
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(10000)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify((int) id, notification);
        eventEntity.updateRemainderSend(id);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.channel_name);
            String description = getApplicationContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; we can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
