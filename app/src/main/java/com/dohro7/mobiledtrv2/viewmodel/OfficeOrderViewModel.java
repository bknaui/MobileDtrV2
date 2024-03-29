package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.OfficeOrderRepository;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OfficeOrderViewModel extends AndroidViewModel {
    private OfficeOrderRepository officeOrderRepository;
    private LiveData<List<OfficeOrderModel>> listLiveData;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<UserModel> userModel;
    private MutableLiveData<String> mutableUploadError;

    public OfficeOrderViewModel(@NonNull Application application) {
        super(application);
        officeOrderRepository = new OfficeOrderRepository(application);
        listLiveData = officeOrderRepository.getListLiveData();
        userSharedPreference = UserSharedPreference.getInstance(application);
        userModel = userSharedPreference.getUserModel();
        mutableUploadError = officeOrderRepository.getMutableUploadError();
    }

    public MutableLiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public LiveData<List<OfficeOrderModel>> getListLiveData() {
        return listLiveData;
    }

    public void insertOfficeOrder(OfficeOrderModel officeOrderModel) {
        officeOrderRepository.insertOfficeOrder(officeOrderModel);
    }

    public void deleteOfficeOrder(OfficeOrderModel officeOrderModel) {
        officeOrderRepository.deleteOfficeOrder(officeOrderModel);
    }

    public void uploadLogs() {
        try {
            JSONObject data = new JSONObject();
            data.put("userid", userModel.getValue().id);
            JSONArray so = new JSONArray();
            for (OfficeOrderModel officeOrderModel : listLiveData.getValue()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("so_no", officeOrderModel.so_no);
                jsonObject.put("daterange", officeOrderModel.inclusive_date);
                so.put(jsonObject);
            }
            data.put("so", so);
            Log.e("SO", data.toString());
            officeOrderRepository.uploadSo(data);
        } catch (JSONException e) {
        }
    }
}
