package com.dohro7.mobiledtrv2.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_tbl")
public class UserModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("userid")
    public String id;

    @ColumnInfo(name = "fname")
    @SerializedName("fname")
    public String fname;

    @ColumnInfo(name = "lname")
    @SerializedName("lname")
    public String lname;

    public UserModel(String id, String fname, String lname) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }

    public String getName() {
        return fname + " " + lname;
    }

}
