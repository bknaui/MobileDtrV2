package com.dohro7.mobiledtrv2.presenter;

import com.dohro7.mobiledtrv2.contract.LoginContract;
import com.dohro7.mobiledtrv2.model.UserModel;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view){
        this.view = view;
    }

    @Override
    public void login(String imei) {
    //@todo: implement server query

        if(imei.equals("1")){
            view.onSuccess(new UserModel("1","naui"));
        }else{
            view.onFail("Failed");
        }
    }
}
