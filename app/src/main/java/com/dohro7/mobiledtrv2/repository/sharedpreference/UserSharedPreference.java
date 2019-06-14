package com.dohro7.mobiledtrv2.repository.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.dohro7.mobiledtrv2.model.UserModel;

public class UserSharedPreference {
    private final String USER_SHARED_PREF = "user_shared_pref";
    private UserModel userModel;
    private static UserSharedPreference instance;
    private SharedPreferences sharedPreferences;

    private UserSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);

        String userId = sharedPreferences.getString("userid", null);
        if (userId != null) {
            String firstName = sharedPreferences.getString("fname", null);
            String lastName = sharedPreferences.getString("lname", null);
            userModel = new UserModel(userId, firstName, lastName);
        }
    }

    public static UserSharedPreference getInstance(Context context) {
        if (instance == null) {
            instance = new UserSharedPreference(context);
        }
        return instance;
    }

    public void insertUser(UserModel userModel) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userModel.id);
        editor.putString("fname", userModel.fname);
        editor.putString("lname", userModel.lname);
        editor.commit();

    }

    public UserModel getUserModel() {
        return userModel;
    }
}
