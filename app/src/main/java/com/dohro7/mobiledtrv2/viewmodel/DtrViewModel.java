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
    private SharedPreferences sharedPreferencesUser;

    private final String USER_SHARED_PREF = "user_shared_pref";
    private final String DTR_SHARED_PREF = "dtr_shared_pref";

    public DtrViewModel(@NonNull Application application) {
        super(application);
        dtrRepository = new DtrRepository(application);
        liveDataList = dtrRepository.getTimeLogs();
        mutableLiveMenuTitle = new MutableLiveData<>();
        mutableTimeExists = new MutableLiveData<>();
        mutableUndertime = new MutableLiveData<>();
        sharedPreferences = application.getSharedPreferences(DTR_SHARED_PREF, application.MODE_PRIVATE);
        sharedPreferencesUser = application.getSharedPreferences(USER_SHARED_PREF,Context.MODE_PRIVATE);
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

    public void uploadLogs() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray logs = new JSONArray();
            String userid = sharedPreferencesUser.getString("userid",null);
            for (int i = 0; i < liveDataList.getValue().size(); i++) {
                JSONObject logsOject = new JSONObject();
                logsOject.put("userid", userid);
                logsOject.put("time", liveDataList.getValue().get(i).time);
                logsOject.put("event", liveDataList.getValue().get(i).status);
                logsOject.put("date", liveDataList.getValue().get(i).date);
                logsOject.put("remark", "MOBILE");
                logsOject.put("edited", "0");
                logsOject.put("latitude", liveDataList.getValue().get(i).latitude);
                logsOject.put("longitude", liveDataList.getValue().get(i).longitude);
                logsOject.put("filename", liveDataList.getValue().get(i).filePath);
                logsOject.put("image", "asdjkahdasjdhasd");
                logs.put(logsOject);
            }

            jsonObject.put("logs", logs);
           // dtrRepository.uploadLogs(jsonObject);
            Log.e("upload", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sample() {


        try {
            JSONObject jsonObject = new JSONObject();


            jsonObject.put("name", "Asnaui");
            jsonObject.put("age", 12);


            JSONArray sampletry = new JSONArray();

            JSONObject experience = new JSONObject();
            experience.put("company", "Payvenue");
            experience.put("years", 1);

          /*  JSONObject experience1 = new JSONObject();
            experience.put("company", "Doh7");
            experience.put("years", 3);

            sampletry.put(experience);
            sampletry.put(experience1);*/

            jsonObject.put("experience",experience);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

/*
{
  "name":"Asnaui",
  "age":12,
  "experience": {"company":"Payvenue","years":1}

}*/


