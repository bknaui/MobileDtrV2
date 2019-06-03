package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.DtrRepository;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;

import java.util.ArrayList;
import java.util.List;

public class DtrViewModel extends AndroidViewModel {
    private DtrRepository dtrRepository;
    private MutableLiveData<String> mutableLiveMenuTitle;
    private LiveData<List<TimeLogModel>> liveDataList;
    private MutableLiveData<String> mutableTimeExists;
    private MutableLiveData<Boolean> mutableUndertime;

    public DtrViewModel(@NonNull Application application) {
        super(application);
        dtrRepository = new DtrRepository(application);
        liveDataList = dtrRepository.getTimeLogs();
        mutableLiveMenuTitle = new MutableLiveData<>();
        mutableTimeExists = new MutableLiveData<>();
        mutableUndertime = new MutableLiveData<>();
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
                mutableTimeExists.setValue("You have already timed IN this morning");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("IN") && timeLogModel.getHourTime() > 11 && data.getHourTime() > 11) { //PM IN EXISTS
                mutableTimeExists.setValue("You have already timed IN this afternoon");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHourTime() < 17 && data.getHourTime() < 17) { //AM OUT EXISTS
                mutableTimeExists.setValue("You have already timed OUT this morning");
                return;
            }
            if (timeLogModel.status.equalsIgnoreCase("OUT") && timeLogModel.getHourTime() > 16 && data.getHourTime() > 16) { //PM OUT EXISTS
                mutableTimeExists.setValue("You have already timed OUT this afternoon");
                return;
            }
            if (isUndertime(timeLogModel)) { //AM OUT IS UNDERTIME
                mutableUndertime.setValue(true);
                return;
            }

            mutableUndertime.setValue(false);
        }
    }

    public void insertTimeLog(TimeLogModel timeLogModel) {
        dtrRepository.insertTimeLog(timeLogModel);
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
}
