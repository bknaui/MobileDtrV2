package com.dohro7.mobiledtrv2.contract;

import com.dohro7.mobiledtrv2.model.UserModel;

public interface LoginContract {

    interface View{
        void onSuccess(UserModel userModel);
        void onFail(String message);
    }
    interface Presenter{
        void login(String imei);
    }
}
