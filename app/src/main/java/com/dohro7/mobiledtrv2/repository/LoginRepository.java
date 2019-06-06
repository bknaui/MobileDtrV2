package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.source.AppDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private MutableLiveData<UserModel> currentUser;
    private SharedPreferences sharedPreferences;
    private final String USER_SHARED_PREF = "user_shared_pref";

    public LoginRepository(Context context) {
        this.currentUser = new MutableLiveData<>();
        sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.getString("userid", null) != null) {
            String userid = sharedPreferences.getString("userid", null);
            String fname = sharedPreferences.getString("fname", null);
            String lname = sharedPreferences.getString("lname", null);
            UserModel userModel = new UserModel(userid, fname, lname);
            this.currentUser.setValue(userModel);
            return;
        }
        this.currentUser.setValue(null);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public void insertUser(String imei) {
        Call<UserModel> userViewModelCall = RetrofitClient.getRetrofitApi().login(imei);
        userViewModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel = response.body();
                if (userModel != null) {
                    insertUserPref(userModel);
                    currentUser.setValue(userModel);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    public void insertUserPref(UserModel userModel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userModel.id);
        editor.putString("fname", userModel.fname);
        editor.putString("lname", userModel.lname);
        editor.commit();

    }
}
