package com.dohro7.mobiledtrv2.model;

import com.google.gson.annotations.SerializedName;

public class RetrofitMessage {

    @SerializedName("message")
    public String message;

    public RetrofitMessage(String message) {
        this.message = message;
    }
}
