package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.repository.CtoRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CtoViewModel extends AndroidViewModel {
    private CtoRepository ctoRepository;
    private LiveData<List<CtoModel>> listLiveData;
    private String userid;
    private final String USER_SHARED_PREF = "user_shared_pref";

    public CtoViewModel(@NonNull Application application) {
        super(application);
        this.ctoRepository = new CtoRepository(application);
        this.listLiveData = ctoRepository.getListLiveData();

        SharedPreferences sharedPreferences = application.getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", null);

    }

    public LiveData<List<CtoModel>> getListLiveData() {
        return listLiveData;
    }

    public void insertCto(CtoModel ctoModel) {
        ctoRepository.insertCto(ctoModel);
    }

    public void deleteCto(CtoModel ctoModel) {
        ctoRepository.deleteCto(ctoModel);
    }

    public void uploadCto() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray cto = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject daterange = new JSONObject();
                daterange.put("userid", userid);
                daterange.put("daterange", "02123123");
                cto.put(daterange);
            }

            jsonObject.put("cto", cto);
            //ctoRepository.uploadCto(jsonObject);
            Log.e("upload", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
