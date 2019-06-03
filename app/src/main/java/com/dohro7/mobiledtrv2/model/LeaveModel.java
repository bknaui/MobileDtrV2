package com.dohro7.mobiledtrv2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leave_tbl")
public class LeaveModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "inclusive_date")
    public String inclusive_date;

    public LeaveModel(int id, String type, String inclusive_date) {
        this.id = id;
        this.type = type;
        this.inclusive_date = inclusive_date;
    }
}
