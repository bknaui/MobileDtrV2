package com.dohro7.mobiledtrv2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.repository.OfficeOrderRepository;

import java.util.List;

public class OfficeOrderViewModel extends AndroidViewModel {
    private OfficeOrderRepository officeOrderRepository;
    private LiveData<List<OfficeOrderModel>> listLiveData;

    public OfficeOrderViewModel(@NonNull Application application) {
        super(application);
        officeOrderRepository = new OfficeOrderRepository(application);
        listLiveData = officeOrderRepository.getListLiveData();
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
}
