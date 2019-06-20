package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {
    private LoginRepository loginRepository;
    private LiveData<UserModel> currentUser;
    private LiveData<String> loginErrorMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
        loginErrorMessage = loginRepository.getLoginErrorMessage();
        currentUser = loginRepository.getCurrentUser();
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getLoginErrorMessage() {
        return loginErrorMessage;
    }
    public void login(String imei) {
        loginRepository.insertUser(imei);
    }
}
