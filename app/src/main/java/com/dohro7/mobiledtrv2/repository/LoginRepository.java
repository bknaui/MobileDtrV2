package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private MutableLiveData<UserModel> currentUser;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<String> loginErrorMessage;
    private UserModel userModel;
    private RetrofitApi retrofitApi;

    public LoginRepository(Context context) {
        userSharedPreference = UserSharedPreference.getInstance(context);
        userModel = userSharedPreference.getUserModel();
        currentUser = new MutableLiveData<>();
        loginErrorMessage = new MutableLiveData<>();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
        currentUser.setValue(userModel);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void insertUser(String imei) {
        UserModel userModel = new UserModel("123","asd","asd");
        userSharedPreference.insertUser(userModel);
        currentUser.setValue(userModel);
        /*Call<UserModel> userViewModelCall = retrofitApi.login(imei);
        userViewModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(!response.isSuccessful()){
                    if(response.code() == 500)
                        loginErrorMessage.setValue("Server error, please contact system administrator");
                    return;
                }

                UserModel userModel = response.body();
                if (userModel == null) {
                    //User not registered
                    loginErrorMessage.setValue("IMEI not registered");
                    return;
                }
                userSharedPreference.insertUser(userModel);
                currentUser.setValue(userModel);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                loginErrorMessage.setValue(t.getMessage());
                Log.e("onFailure", t.getMessage());
            }
        });
        */
    }

}
