package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.LeaveRepository;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LeaveViewModel extends AndroidViewModel {
    private LeaveRepository leaveRepository;
    private LiveData<List<LeaveModel>> listLiveData;
    private LiveData<String> mutableUploadError;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<UserModel> userModel;

    public LeaveViewModel(@NonNull Application application) {
        super(application);
        this.leaveRepository = new LeaveRepository(application);
        this.listLiveData = leaveRepository.getListLiveData();
        userSharedPreference = UserSharedPreference.getInstance(application);
        userModel = userSharedPreference.getUserModel();
        this.mutableUploadError = leaveRepository.getMutableUploadError();
    }

    public LiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public LiveData<List<LeaveModel>> getListLiveData() {
        return listLiveData;
    }

    public void insertLeave(LeaveModel leaveModel) {
        leaveRepository.insertLeave(leaveModel);
    }

    public void deleteLeave(LeaveModel leaveModel) {
        leaveRepository.deleteLeave(leaveModel);
    }

    public void uploadLeave() {

        try {
            JSONObject data = new JSONObject();
            data.put("userid", userModel.getValue().id);
            JSONArray leaves = new JSONArray();
            for (LeaveModel leaveModel : listLiveData.getValue()) {
                JSONObject leave = new JSONObject();
                leave.put("leave_type", leaveModel.type);
                leave.put("daterange", leaveModel.inclusive_date);
                leaves.put(leave);
            }
            data.put("leave", leaves);
            leaveRepository.uploadLeaves(data);
            Log.e("Data", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
