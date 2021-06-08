package com.example.mcs18440032.a1.models;

import com.example.mcs18440032.a1.helpers.Helper;

import java.text.ParseException;

public class Event {
    private long id;
    private String eventName;
    private String eventLocation;
    private String description;
    private long date;
    private long startTime;
    private long endTime;
    private long remainder1;
    private long remainder2;
    private long remainder3;

    public Event() {

    }

    public Event(long id, String eventName, String eventLocation, String description,
                 String date, String startTime, String endTime, String remainder1, String remainder2, String remainder3) throws ParseException {
        this.id = id;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.description = description;
        this.date = Helper.convertDateToLong(date);
        this.startTime = Helper.convertTimeToLong(startTime);
        this.endTime = Helper.convertTimeToLong(endTime);

        if (remainder1 != null) {
            this.remainder1 = Helper.convertRemainderToLong(remainder1);
        }
        if (remainder2 != null) {
            this.remainder2 = Helper.convertRemainderToLong(remainder2);
        }
        if (remainder3 != null) {
            this.remainder3 = Helper.convertRemainderToLong(remainder3);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) throws ParseException {
        try {
            this.startTime = Helper.convertTimeToLong(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) throws ParseException {
        try {
            this.endTime = Helper.convertTimeToLong(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getRemainder1() {
        return remainder1;
    }

    public void setRemainder1(long remainder1) {
        this.remainder1 = remainder1;
    }

    public long getRemainder2() {
        return remainder2;
    }

    public void setRemainder2(long remainder2) {
        this.remainder2 = remainder2;
    }

    public long getRemainder3() {
        return remainder3;
    }

    public void setRemainder3(long remainder3) {
        this.remainder3 = remainder3;
    }
}
