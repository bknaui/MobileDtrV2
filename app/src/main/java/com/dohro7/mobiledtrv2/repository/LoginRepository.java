package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.ResponseBody;
import com.dohro7.mobiledtrv2.model.RetrofitMessage;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private MutableLiveData<UserModel> currentUser;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<String> loginErrorMessage;
    private RetrofitApi retrofitApi;

    public LoginRepository(Context context) {
        userSharedPreference = UserSharedPreference.getInstance(context); //if naay UserSharedPreference?! else create
        currentUser = userSharedPreference.getUserModel();
        loginErrorMessage = new MutableLiveData<>();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void insertUser(String imei) {

        Call<ResponseBody> userViewModelCall = retrofitApi.login(imei);
        userViewModelCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                Log.e("Response", responseBody.code + "");
                if (responseBody.code == 200) {
                    userSharedPreference.insertUser(responseBody.response);
                    currentUser.setValue(responseBody.response);
                    return;
                }
                if (responseBody.code == 201) {
                    loginErrorMessage.setValue("IMEI not registered");
                    return;
                }
                if (response.code() == 500 || responseBody.code == 500) {
                    loginErrorMessage.setValue("Server error, please contact system administrator");
                    return;
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loginErrorMessage.setValue(t.getMessage());
                Log.e("onFailure", t.getMessage());
            }
        });

    }

}
