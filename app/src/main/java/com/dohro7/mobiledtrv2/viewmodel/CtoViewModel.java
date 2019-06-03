package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.repository.CtoRepository;

import java.util.List;

public class CtoViewModel extends AndroidViewModel {
    private CtoRepository ctoRepository;
    private LiveData<List<CtoModel>> listLiveData;

    public CtoViewModel(@NonNull Application application) {
        super(application);
        this.ctoRepository = new CtoRepository(application);
        this.listLiveData = ctoRepository.getListLiveData();
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
}
