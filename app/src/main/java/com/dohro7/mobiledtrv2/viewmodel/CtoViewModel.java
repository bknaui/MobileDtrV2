package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.CtoRepository;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CtoViewModel extends AndroidViewModel {
    private CtoRepository ctoRepository;
    private LiveData<List<CtoModel>> listLiveData;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<UserModel> userModel;
    private LiveData<String> uploadMessage;

    public CtoViewModel(@NonNull Application application) {
        super(application);
        this.ctoRepository = new CtoRepository(application);
        this.listLiveData = ctoRepository.getListLiveData();
        userSharedPreference = UserSharedPreference.getInstance(application);
        userModel = userSharedPreference.getUserModel();
        uploadMessage = ctoRepository.getMutableUploadError();
    }

    public LiveData<String> getUploadMessage() {
        return uploadMessage;
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
            jsonObject.put("userid", userModel.getValue().id);
            JSONArray cto = new JSONArray();
            for (CtoModel ctoModel : listLiveData.getValue()) {
                JSONObject daterange = new JSONObject();
                daterange.put("daterange", ctoModel.inclusive_date);
                cto.put(daterange);
            }

            jsonObject.put("cdo", cto);
            ctoRepository.uploadCto(jsonObject);
            Log.e("upload", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
