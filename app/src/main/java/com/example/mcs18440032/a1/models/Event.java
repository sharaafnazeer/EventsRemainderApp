package com.example.mcs18440032.a1.models;

import java.text.ParseException;

public class Event {
    private long id;
    private String eventName;
    private String eventLocation;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String remainder1;
    private String remainder2;
    private String remainder3;
    private int isRemainder;

    public Event() {

    }

    public Event(long id, String eventName, String eventLocation, String description,
                 String date, String startTime, String endTime, String remainder1, String remainder2, String remainder3) throws ParseException {
        this.id = id;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

        if (remainder1 != null) {
            this.remainder1 = remainder1;
        }
        if (remainder2 != null) {
            this.remainder2 = remainder2;
        }
        if (remainder3 != null) {
            this.remainder3 = remainder3;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemainder1() {
        return remainder1;
    }

    public void setRemainder1(String remainder1) {
        this.remainder1 = remainder1;
    }

    public String getRemainder2() {
        return remainder2;
    }

    public void setRemainder2(String remainder2) {
        this.remainder2 = remainder2;
    }

    public String getRemainder3() {
        return remainder3;
    }

    public void setRemainder3(String remainder3) {
        this.remainder3 = remainder3;
    }

    public int getIsRemainder() {
        return isRemainder;
    }

    public void setIsRemainder(int isRemainder) {
        this.isRemainder = isRemainder;
    }
}
