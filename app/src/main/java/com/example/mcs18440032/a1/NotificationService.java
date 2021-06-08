package com.example.mcs18440032.a1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mcs18440032.a1.db.event.EventEntity;
import com.example.mcs18440032.a1.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationService extends Service {

    EventEntity eventEntity;

    public NotificationService() {
        this.eventEntity = new EventEntity(getApplicationContext());
    }



    @Override
    public void onCreate() {
        Log.i("SERVICE", "Service Created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "Service Running");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
