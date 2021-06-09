package com.example.mcs18440032.a1.db.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.mcs18440032.a1.helpers.Helper;
import com.example.mcs18440032.a1.models.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EventEntity implements BaseColumns {

    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_ID = "id";
    public static final String EVENTS_COLUMN_NAME = "name";
    public static final String EVENTS_COLUMN_LOCATION = "location";
    public static final String EVENTS_COLUMN_DESCRIPTION = "description";
    public static final String EVENTS_COLUMN_DATE = "date";
    public static final String EVENTS_COLUMN_START_TIME = "start_time";
    public static final String EVENTS_COLUMN_END_TIME = "end_time";
    public static final String EVENTS_COLUMN_REMAINDER_1 = "remainder_1";
    public static final String EVENTS_COLUMN_REMAINDER_2 = "remainder_2";
    public static final String EVENTS_COLUMN_REMAINDER_3 = "remainder_3";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            EventEntity.EVENTS_TABLE_NAME +
            "(" + EventEntity.EVENTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            EventEntity.EVENTS_COLUMN_NAME + " TEXT NOT NULL, " +
            EventEntity.EVENTS_COLUMN_LOCATION + " TEXT NOT NULL, " +
            EventEntity.EVENTS_COLUMN_DESCRIPTION + " TEXT, " +
            EventEntity.EVENTS_COLUMN_DATE + " TEXT, " +
            EventEntity.EVENTS_COLUMN_START_TIME + " TEXT, " +
            EventEntity.EVENTS_COLUMN_END_TIME + " TEXT NULL, " +
            EventEntity.EVENTS_COLUMN_REMAINDER_1 + " TEXT NULL, " +
            EventEntity.EVENTS_COLUMN_REMAINDER_2 + " TEXT NULL, " +
            EventEntity.EVENTS_COLUMN_REMAINDER_3 + " TEXT NULL " +
            ")";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " +
            EventEntity.EVENTS_TABLE_NAME;


    private final EventHelper helper;

    public EventEntity(Context context) {
        this.helper = new EventHelper(context, null, null, -1);
    }

    public long create(Event event) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventEntity.EVENTS_COLUMN_ID, event.getId());
        values.put(EventEntity.EVENTS_COLUMN_NAME, event.getEventName());
        values.put(EventEntity.EVENTS_COLUMN_LOCATION, event.getEventLocation());
        values.put(EventEntity.EVENTS_COLUMN_DESCRIPTION, event.getDescription());
        values.put(EventEntity.EVENTS_COLUMN_DATE, event.getDate());
        values.put(EventEntity.EVENTS_COLUMN_START_TIME, event.getStartTime());
        values.put(EventEntity.EVENTS_COLUMN_END_TIME, event.getEndTime());
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_1, Helper.convertRemainderToDbFormat(event.getRemainder1()));
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_2, Helper.convertRemainderToDbFormat(event.getRemainder2()));
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_3, Helper.convertRemainderToDbFormat(event.getRemainder3()));

        return database.insert(EventEntity.EVENTS_TABLE_NAME, null, values);
    }

    public long update(Event event, long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventEntity.EVENTS_COLUMN_NAME, event.getEventName());
        values.put(EventEntity.EVENTS_COLUMN_LOCATION, event.getEventLocation());
        values.put(EventEntity.EVENTS_COLUMN_DESCRIPTION, event.getDescription());
        values.put(EventEntity.EVENTS_COLUMN_DATE, event.getDate());
        values.put(EventEntity.EVENTS_COLUMN_START_TIME, event.getStartTime());
        values.put(EventEntity.EVENTS_COLUMN_END_TIME, event.getEndTime());
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_1, Helper.convertRemainderToDbFormat(event.getRemainder1()));
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_2, Helper.convertRemainderToDbFormat(event.getRemainder2()));
        values.put(EventEntity.EVENTS_COLUMN_REMAINDER_3, Helper.convertRemainderToDbFormat(event.getRemainder3()));

        String selectionValue = EventEntity.EVENTS_COLUMN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        return database.update(EventEntity.EVENTS_TABLE_NAME, values, selectionValue, selectionArgs);
    }

    public long delete(long id) {
        SQLiteDatabase database = helper.getWritableDatabase();

        String selectionValue = EventEntity.EVENTS_COLUMN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        return database.delete(EventEntity.EVENTS_TABLE_NAME, selectionValue, selectionArgs);
    }

    public List<Event> getAllByDate(String eventDate) throws ParseException {
        SQLiteDatabase database = helper.getReadableDatabase();
        List<Event> eventList = new ArrayList<>();

        String[] projectionList = {
                EventEntity.EVENTS_COLUMN_ID, EventEntity.EVENTS_COLUMN_NAME,
                EventEntity.EVENTS_COLUMN_LOCATION, EventEntity.EVENTS_COLUMN_DESCRIPTION,
                EventEntity.EVENTS_COLUMN_START_TIME, EventEntity.EVENTS_COLUMN_DATE,
                EventEntity.EVENTS_COLUMN_END_TIME, EventEntity.EVENTS_COLUMN_REMAINDER_1,
                EventEntity.EVENTS_COLUMN_REMAINDER_2, EventEntity.EVENTS_COLUMN_REMAINDER_3
        };

        String selectionValue = null;
        String[] selectionArgs = {};

        if (eventDate != null) {
            selectionValue = EventEntity.EVENTS_COLUMN_DATE + " LIKE ? ";
            selectionArgs = new String[]{eventDate};
        }

        String sort = EventEntity.EVENTS_COLUMN_NAME + " DESC";

        Cursor cursor = database.query(
                EventEntity.EVENTS_TABLE_NAME,
                projectionList,
                selectionValue,
                selectionArgs,
                null,
                null,
                sort
        );

        if (cursor != null && cursor.isBeforeFirst()) {
            Event event;
            while (cursor.moveToNext()) {
                event = new Event();
                event.setId(cursor.getLong(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_ID)));
                event.setEventName(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_NAME)));
                event.setEventLocation(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_LOCATION)));
                event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_DESCRIPTION)));
                event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_DATE)));
                event.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_START_TIME)));
                event.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_END_TIME)));
                event.setRemainder1(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_1)));
                event.setRemainder2(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_2)));
                event.setRemainder3(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_3)));
                eventList.add(event);
            }
            cursor.close();
        }
        return eventList;
    }

    public Event get(long id) {
        SQLiteDatabase database = helper.getReadableDatabase();
        String[] projectionList = {
                EventEntity.EVENTS_COLUMN_ID, EventEntity.EVENTS_COLUMN_NAME,
                EventEntity.EVENTS_COLUMN_LOCATION, EventEntity.EVENTS_COLUMN_DESCRIPTION,
                EventEntity.EVENTS_COLUMN_START_TIME, EventEntity.EVENTS_COLUMN_DATE,
                EventEntity.EVENTS_COLUMN_END_TIME, EventEntity.EVENTS_COLUMN_REMAINDER_1,
                EventEntity.EVENTS_COLUMN_REMAINDER_2, EventEntity.EVENTS_COLUMN_REMAINDER_3
        };

        String selectionValue = EventEntity.EVENTS_COLUMN_ID + " = ?";
        String[] selectionArgs = {id + ""};

        Cursor cursor = database.query(
                EventEntity.EVENTS_TABLE_NAME,
                projectionList,
                selectionValue,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.isBeforeFirst()) {
            cursor.moveToFirst();

            Event event = new Event();
            event.setId(cursor.getLong(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_ID)));
            event.setEventName(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_NAME)));
            event.setEventLocation(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_LOCATION)));
            event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_DESCRIPTION)));
            event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_DATE)));
            event.setStartTime(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_START_TIME)));
            event.setEndTime(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_END_TIME)));
            event.setRemainder1(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_1)));
            event.setRemainder2(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_2)));
            event.setRemainder3(cursor.getString(cursor.getColumnIndexOrThrow(EventEntity.EVENTS_COLUMN_REMAINDER_3)));
            cursor.close();
            return event;
        }
        return null;
    }
}
