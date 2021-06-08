package com.example.mcs18440032.a1.db;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPreferences preferences;
    private static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "EventsRemainder";
    private static final String KEY_LAST_ID = "last_id";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public static long getKeyLastInsertedID(Context context) {
        return getPreferences(context).getLong(KEY_LAST_ID, 0);
    }

    public static void setKeyLastInsertedID(Context context, long id) {
        getPreferences(context).edit().putLong(KEY_LAST_ID, id).apply();
    }
}
