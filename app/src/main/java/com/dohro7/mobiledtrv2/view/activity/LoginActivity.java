package com.dohro7.mobiledtrv2.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.contract.LoginContract;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        loginPresenter = new LoginPresenter(this);
    }

    //onclick
    public void login(View view) {
        loginPresenter.login("1");
    }

    //LoginContract.view
    @Override
    public void onSuccess(UserModel userModel) {
        Toast.makeText(this, userModel.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
