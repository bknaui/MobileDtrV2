package com.dohro7.mobiledtrv2.model;

public class TimeLogRequest {
    public String date;
    public String time;
    public String status;

    public TimeLogRequest(String date, String time, String status) {
        this.date = date;
        this.time = time;
        this.status = status;
    }
}
