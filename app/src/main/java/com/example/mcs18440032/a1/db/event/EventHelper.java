package com.example.mcs18440032.a1.db.event;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EventHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "events.db";

    public EventHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("Create table query ======> " + EventEntity.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(EventEntity.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        System.out.println("Delete and create table query ======> " + EventEntity.SQL_CREATE_TABLE + " ==== " + EventEntity.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(EventEntity.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(EventEntity.SQL_CREATE_TABLE);
    }
}
