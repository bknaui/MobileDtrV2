package com.dohro7.mobiledtrv2.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginViewModel loginViewModel;
    private String imei;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getCurrentUser().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if (userModel != null) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    setContentView(R.layout.login_main);
                    loginButton = findViewById(R.id.login_btn);
                    progressBar = findViewById(R.id.progressbar);
                    loginButton.setOnClickListener(LoginActivity.this);
                }
            }
        });
        loginViewModel.getLoginErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setButtonEnabled(true);
                setProgressVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.root), s, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                });
                snackbar.show();
            }
        });
    }


    //onclick
    public void login() {
        //Check if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            return;
        }
        setButtonEnabled(false);
        setProgressVisibility(View.VISIBLE);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imei = telephonyManager.getImei();
        } else {
            imei = telephonyManager.getDeviceId();
        }
        Log.e("IMEI", imei);
        //imei = "359447098842222";
        loginViewModel.login(imei);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        login();
    }

    @Override
    public void onClick(View v) {
        login();
    }

    public void setButtonEnabled(boolean enabled){
        loginButton.setEnabled(enabled);
    }

    public void setProgressVisibility(int visibility){
        progressBar.setVisibility(visibility);
    }
}
