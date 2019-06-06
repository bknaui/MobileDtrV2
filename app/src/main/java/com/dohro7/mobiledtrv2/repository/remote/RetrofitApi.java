package com.dohro7.mobiledtrv2.repository.remote;

import com.dohro7.mobiledtrv2.model.UserModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {

    @POST("/dtr/mobile/add-logs")
    void uploadTimelogs(@Query("userid") String userid, @Query("time") String time, @Query("event") String event,
                        @Query("date") String date, @Query("latitude") String latitude, @Query("longitude") String longitude,
                        @Query("filename") String filename);

    @POST("/dtr/mobile/add-leave")
    void uploadLeaves(@Query("userid") String userid, @Query("leave_type") String leave_type, @Query("daterange") String daterange);

    @POST("/dtr/mobile/add-so")
    void uploadOfficeOrder(@Query("userid") String userid, @Query("so") String so, @Query("daterange") String daterange);

    @POST("/dtr/mobile/add-cto")
    Call<String> uploadCto(@Query("cto")JSONObject jsonObject);

    @POST("/dtr/mobile/login")
    Call<UserModel> login(@Query("imei") String imei);


}