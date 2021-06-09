package com.example.mcs18440032.a1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private EventEntity eventEntity;
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
    private Ringtone ringtone;
    private Timer timer;

    @Override
    public void onCreate() {
        eventEntity = new EventEntity(getApplication().getApplicationContext());
        timer = new Timer();
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
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
        ringtone.stop();
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
        System.out.println(now.toString(dateTimeFormatter));

        DateTime actualCurrentTime = dateTimeFormatter.parseDateTime(now.toString(dateTimeFormatter));

        System.out.println(actualCurrentTime);

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
                if (actualCurrentTime.isEqual(alertLongTime)) {
                    ringtone.play();
                    createNotifications(event.getId(), event.getEventName(), event.getStartTime());
                } else {
                    if (ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                }
            }
        }
    }

    private void createNotifications(long id, String title, String startTime) {
        // Create an explicit intent for an Activity in the app
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Event Remainder")
                .setContentText(title + " at " + startTime)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(20000)
//                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true)
                .setOngoing(true)
                .addAction(R.drawable.ic_okay, "Got It!", pendingIntent)
                .build();

//        startForeground((int) id, notification);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify((int) id, notification);
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
