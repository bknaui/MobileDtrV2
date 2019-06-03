package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.repository.LeaveRepository;

import java.util.List;

public class LeaveViewModel extends AndroidViewModel {
    private LeaveRepository leaveRepository;
    private LiveData<List<LeaveModel>> listLiveData;

    public LeaveViewModel(@NonNull Application application) {
        super(application);
        this.leaveRepository = new LeaveRepository(application);
        this.listLiveData = leaveRepository.getListLiveData();
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
}
