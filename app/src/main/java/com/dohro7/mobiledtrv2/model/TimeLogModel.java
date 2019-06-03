package com.dohro7.mobiledtrv2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_log")
public class TimeLogModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "time")
    public String time;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "filePath")
    public String filePath;
    @ColumnInfo(name = "latitude")
    public String latitude;
    @ColumnInfo(name = "longitude")
    public String longitude;

    public TimeLogModel() {
    }

    public int getHourTime() {
        return Integer.parseInt(time.split(":")[0]);
    }
}
