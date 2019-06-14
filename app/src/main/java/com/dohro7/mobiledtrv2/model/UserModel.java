package com.dohro7.mobiledtrv2.model;

import com.google.gson.annotations.SerializedName;


public class UserModel {
    @SerializedName("userid")
    public String id;
    @SerializedName("fname")
    public String fname;
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
