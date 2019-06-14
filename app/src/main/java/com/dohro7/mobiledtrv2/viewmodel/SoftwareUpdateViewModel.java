package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.SoftwareUpdateModel;
import com.dohro7.mobiledtrv2.repository.SoftwareUpdateRepository;

public class SoftwareUpdateViewModel extends AndroidViewModel {
    private MutableLiveData<SoftwareUpdateModel> mutableUpdateModel;
    private SoftwareUpdateRepository softwareUpdateRepository;
    private MutableLiveData<Double> mutableDownloadPercentage;

    public SoftwareUpdateViewModel(@NonNull Application application) {
        super(application);
        softwareUpdateRepository = new SoftwareUpdateRepository(application);
        mutableUpdateModel = softwareUpdateRepository.getMutableSoftwareModel();
        mutableDownloadPercentage = softwareUpdateRepository.getMutableDownloadPercentage();
    }

    public MutableLiveData<SoftwareUpdateModel> getMutableUpdateModel() {
        return mutableUpdateModel;
    }

    public MutableLiveData<Double> getMutableDownloadPercentage() {
        return mutableDownloadPercentage;
    }

    public void checkSotwareUpdate() {
        softwareUpdateRepository.checkSoftwareUpdate();
    }

    public void downloadApkFromServer() {
        softwareUpdateRepository.downloadApkFromServer();
    }
}
