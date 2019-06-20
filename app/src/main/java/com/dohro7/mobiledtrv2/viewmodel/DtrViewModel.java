package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.model.UserModel;
import com.dohro7.mobiledtrv2.repository.DtrRepository;
import com.dohro7.mobiledtrv2.repository.sharedpreference.DtrEventSharedPreference;
import com.dohro7.mobiledtrv2.repository.sharedpreference.UserSharedPreference;
import com.dohro7.mobiledtrv2.utility.BitmapDecoder;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DtrViewModel extends AndroidViewModel {
    private DtrRepository dtrRepository;
    private MutableLiveData<String> uploadMessage;
    private MutableLiveData<String> mutableLiveMenuTitle;
    private LiveData<List<TimeLogModel>> liveDataList;
    private MutableLiveData<String> mutableTimeExists;
    private MutableLiveData<Boolean> mutableUndertime;
    private UserSharedPreference userSharedPreference;
    private MutableLiveData<UserModel> userModel;
    private DtrEventSharedPreference dtrEventSharedPreference;



    public DtrViewModel(@NonNull Application application) {
        super(application);
        dtrRepository = new DtrRepository(application);
        liveDataList = dtrRepository.getTimeLogs();
        mutableLiveMenuTitle = new MutableLiveData<>();
        mutableTimeExists = new MutableLiveData<>();
        mutableUndertime = new MutableLiveData<>();
        uploadMessage = dtrRepository.getUploadMessage();
        userSharedPreference = UserSharedPreference.getInstance(application);
        userModel = userSharedPreference.getUserModel();
        dtrEventSharedPreference = DtrEventSharedPreference.getInstance(application);

        mutableLiveMenuTitle.setValue("IN");
        String menuTitle = dtrEventSharedPreference.getMenuTitle();
        if (menuTitle == null) {
            return;
        }
        if (!dtrEventSharedPreference.getDtrLastDate().equalsIgnoreCase(DateTimeUtility.getCurrentDate())) {
            return;
        }
        if (menuTitle != null) mutableLiveMenuTitle.setValue(menuTitle);
    }

    public LiveData<String> getUploadMessage() {
        return uploadMessage;
    }

    public MutableLiveData<UserModel> getUser() {
        return userModel;
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

            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHour() < 12 && data.getHour() < 12) { // AM IN EXISTS
                mutableTimeExists.setValue("You have already timed IN");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && dbList.size() == 1 && isUndertime(data)) { //Prompts undertime to a time out on afternoon
                mutableUndertime.setValue(true);
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHour() < 17 && data.getHour() < 17) { //AM OUT EXISTS
                mutableTimeExists.setValue("You have already timed OUT");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHour() > 11 && data.getHour() > 11) { //PM IN EXISTS
                mutableTimeExists.setValue("You have already timed IN");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHour() > 16 && data.getHour() > 16) { //PM OUT EXISTS
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

        dtrEventSharedPreference.insertUpdateMenuStatus(mutableLiveMenuTitle.getValue(), DateTimeUtility.getCurrentDate());

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
        int currentTime = Integer.parseInt(timeLogModel.time.split(":")[0]);
        if (timeLogModel.status.equalsIgnoreCase("OUT") && (currentTime < 12 || currentTime < 17)) { //AM IN
            return true;
        }
        return false;
    }

    public void uploadLogs() {
        if(liveDataList.getValue().size() > 0){
            try {
                JSONObject data = new JSONObject();
                JSONArray logs = new JSONArray();

                for (int i = 0; i < liveDataList.getValue().size(); i++) {

                    JSONObject timeLogs = new JSONObject();
                    timeLogs.put("userid", getUser().getValue().id);
                    timeLogs.put("time", liveDataList.getValue().get(i).time);
                    timeLogs.put("event", liveDataList.getValue().get(i).status);
                    timeLogs.put("date", liveDataList.getValue().get(i).date);
                    timeLogs.put("remark", "MOBILE");
                    timeLogs.put("edited", "0");
                    timeLogs.put("latitude", liveDataList.getValue().get(i).latitude + "");
                    timeLogs.put("longitude", liveDataList.getValue().get(i).longitude + "");
                    timeLogs.put("filename", liveDataList.getValue().get(i).fileName + "");
                    String imagePath  = BitmapDecoder.convertBitmapToString(liveDataList.getValue().get(i).filePath);
                    timeLogs.put("image", imagePath);
                    logs.put(timeLogs);
                }

                data.put("logs", logs);
                dtrRepository.uploadLogs(data);
                Log.e("upload", data.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            uploadMessage.setValue("Nothing to upload");
        }

    }
}


