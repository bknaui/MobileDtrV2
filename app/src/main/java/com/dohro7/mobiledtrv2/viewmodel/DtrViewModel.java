package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.DtrRepository;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DtrViewModel extends AndroidViewModel {
    private DtrRepository dtrRepository;
    private MutableLiveData<String> mutableLiveMenuTitle;
    private LiveData<List<TimeLogModel>> liveDataList;
    private MutableLiveData<String> mutableTimeExists;
    private MutableLiveData<Boolean> mutableUndertime;
    private SharedPreferences sharedPreferences;
    private final String DTR_SHARED_PREF = "dtr_shared_pref";

    public DtrViewModel(@NonNull Application application) {
        super(application);
        dtrRepository = new DtrRepository(application);
        liveDataList = dtrRepository.getTimeLogs();
        mutableLiveMenuTitle = new MutableLiveData<>();
        mutableTimeExists = new MutableLiveData<>();
        mutableUndertime = new MutableLiveData<>();
        sharedPreferences = application.getSharedPreferences(DTR_SHARED_PREF, application.MODE_PRIVATE);
        mutableLiveMenuTitle.setValue("IN");
        String menuTitle = sharedPreferences.getString("dtr_status", null);
        if (sharedPreferences.getString("dtr_date", null) == null) {
            return;
        }
        if (!sharedPreferences.getString("dtr_date", null).equalsIgnoreCase(DateTimeUtility.getCurrentDate())) {
            return;
        }

        if (menuTitle != null) mutableLiveMenuTitle.setValue(menuTitle);

    }

    public MutableLiveData<String> getMutableTimeExists() {
        return mutableTimeExists;
    }

    public MutableLiveData<Boolean> getMutableUndertime() {
        return mutableUndertime;
    }

    public MutableLiveData<String> getLiveDataMenuTitle() {
        return mutableLiveMenuTitle;
    }

    public LiveData<List<TimeLogModel>> getMutableLiveDataList() {
        return liveDataList;
    }

    public void timeValidate() {
        TimeLogModel timeLogModel = new TimeLogModel();
        timeLogModel.time = DateTimeUtility.getCurrentTime();
        timeLogModel.date = DateTimeUtility.getCurrentDate();
        timeLogModel.status = mutableLiveMenuTitle.getValue();
        List<TimeLogModel> dbList = getLogsByDateAndStatus(timeLogModel.date, timeLogModel.status);
        for (TimeLogModel data : dbList) {

            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHourTime() < 12 && data.getHourTime() < 12) { // AM IN EXISTS
                mutableTimeExists.setValue("You have already timed IN");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && dbList.size() == 1 && isUndertime(data)) { //Prompts undertime to a time out on afternoon
                mutableUndertime.setValue(true);
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHourTime() < 17 && data.getHourTime() < 17) { //AM OUT EXISTS
                mutableTimeExists.setValue("You have already timed OUT");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHourTime() > 11 && data.getHourTime() > 11) { //PM IN EXISTS
                mutableTimeExists.setValue("You have already timed IN");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHourTime() > 16 && data.getHourTime() > 16) { //PM OUT EXISTS
                mutableTimeExists.setValue("You have already timed OUT");
                return;
            }
        }
        if (isUndertime(timeLogModel)) { //AM OUT IS UNDERTIME
            mutableUndertime.setValue(true);
            return;
        }
        mutableUndertime.setValue(false);
    }

    public void insertTimeLog(TimeLogModel timeLogModel) {
        dtrRepository.insertTimeLog(timeLogModel);
        if (timeLogModel.status.equalsIgnoreCase("IN")) mutableLiveMenuTitle.setValue("OUT");
        else mutableLiveMenuTitle.setValue("IN");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dtr_status", mutableLiveMenuTitle.getValue());
        editor.putString("dtr_date", DateTimeUtility.getCurrentDate());
        editor.commit();

    }

    public List<TimeLogModel> getLogsByDateAndStatus(String date, String status) {
        List<TimeLogModel> list = new ArrayList<>();
        for (TimeLogModel timeLogModel : liveDataList.getValue()) {
            if (timeLogModel.date.equalsIgnoreCase(date) && timeLogModel.status.equalsIgnoreCase(status)) {
                list.add(timeLogModel);
            }
        }
        return list;

    }

    public boolean isUndertime(TimeLogModel timeLogModel) {
        Log.e("Time", timeLogModel.time);
        Log.e("Date", timeLogModel.date);
        Log.e("Status", timeLogModel.date);
        int currentTime = Integer.parseInt(timeLogModel.time.split(":")[0]);
        if (timeLogModel.status.equalsIgnoreCase("OUT") && (currentTime < 12 || currentTime < 17)) { //AM IN
            return true;
        }
        return false;
    }

    public void uploadLogs(){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray logs = new JSONArray();
            for (int i = 0; i < 1; i++) {
                JSONObject logsOject = new JSONObject();
                logsOject.put("userid", "12");
                logsOject.put("time", "02:02:02");
                logsOject.put("event", "IN");
                logsOject.put("date", "2019-02-02");
                logsOject.put("remark", "MOBILE");
                logsOject.put("edited", "0");
                logsOject.put("latitude", "1.123");
                logsOject.put("longitude", "4.567");
                logsOject.put("filename", "Sample.jpg");
                logsOject.put("image", "asdkasjdk123asd");
                logs.put(logsOject);
            }

            jsonObject.put("logs", logs);
            dtrRepository.uploadLogs(jsonObject);
            Log.e("upload", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
