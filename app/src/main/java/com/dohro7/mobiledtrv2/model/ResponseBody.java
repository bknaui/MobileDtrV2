package com.dohro7.mobiledtrv2.model;

import com.google.gson.annotations.SerializedName;

public class ResponseBody {
    @SerializedName("code")
    public int code;
    @SerializedName("response")
    public UserModel response;

    public ResponseBody(int code, UserModel response) {
        this.code = code;
        this.response = response;
    }
}
