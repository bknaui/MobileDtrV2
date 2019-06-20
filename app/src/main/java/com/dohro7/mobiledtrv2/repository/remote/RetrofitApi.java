package com.dohro7.mobiledtrv2.repository.remote;


import com.dohro7.mobiledtrv2.model.ResponseBody;
import com.dohro7.mobiledtrv2.model.SoftwareUpdateModel;
import com.dohro7.mobiledtrv2.model.UploadResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface RetrofitApi {

    @FormUrlEncoded
    @POST("/dtr/mobileV2/add-logs")
    Call<UploadResponse> uploadTimelogs(@Field("data") JSONObject jsonObject);

    @FormUrlEncoded
    @POST("/dtr/mobileV2/add-leave")
    Call<UploadResponse> uploadLeaves(@Field("data") JSONObject jsonObject);

    @FormUrlEncoded
    @POST("/dtr/mobileV2/add-so")
    Call<UploadResponse> uploadSo(@Field("data") JSONObject jsonObject);

    @FormUrlEncoded
    @POST("/dtr/mobileV2/add-cdo")
    Call<UploadResponse> uploadCto(@Field("data") JSONObject jsonObject);

    @POST("/dtr/mobileV2/login")
    Call<ResponseBody> login(@Query("imei") String imei);

    @FormUrlEncoded
    @POST("/dtr/mobileV2/check-update")
    Call<SoftwareUpdateModel> checkSoftwareUpdate();


}
