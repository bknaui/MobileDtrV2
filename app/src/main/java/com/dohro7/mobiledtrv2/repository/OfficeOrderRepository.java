package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.OfficeOrderDao;

import java.util.List;

public class OfficeOrderRepository {
    private OfficeOrderDao officeOrderDao;
    private LiveData<List<OfficeOrderModel>> listLiveData;

    public OfficeOrderRepository(Context context) {
        this.officeOrderDao = AppDatabase.getInstance(context).officeOrderDao();
        this.listLiveData = officeOrderDao.getOfficeOrder();
    }

    public LiveData<List<OfficeOrderModel>> getListLiveData() {
        return listLiveData;
    }

    public void insertOfficeOrder(OfficeOrderModel officeOrderModel) {
        new InsertAsyncTask().execute(officeOrderModel);
    }

    public void deleteOfficeOrder(OfficeOrderModel officeOrderModel) {
        new DeleteAsyncTask().execute(officeOrderModel);
    }

    class InsertAsyncTask extends AsyncTask<OfficeOrderModel, Void, Void> {

        @Override
        protected Void doInBackground(OfficeOrderModel... officeOrderModels) {
            officeOrderDao.insertOfficeOrder(officeOrderModels[0]);
            return null;
        }
    }

    class DeleteAsyncTask extends AsyncTask<OfficeOrderModel, Void, Void> {

        @Override
        protected Void doInBackground(OfficeOrderModel... officeOrderModels) {
            officeOrderDao.deleteOfficerOrder(officeOrderModels[0]);
            return null;
        }
    }
}
