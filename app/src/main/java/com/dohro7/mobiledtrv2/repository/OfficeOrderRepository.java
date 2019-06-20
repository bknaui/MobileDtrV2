package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.model.UploadResponse;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.OfficeOrderDao;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfficeOrderRepository {
    private OfficeOrderDao officeOrderDao;
    private LiveData<List<OfficeOrderModel>> listLiveData;
    private RetrofitApi retrofitApi;
    private MutableLiveData<String> mutableUploadError = new MutableLiveData<>();

    public OfficeOrderRepository(Context context) {
        this.officeOrderDao = AppDatabase.getInstance(context).officeOrderDao();
        this.listLiveData = officeOrderDao.getOfficeOrder();
        this.retrofitApi = RetrofitClient.getRetrofitApi(context);
    }


    public MutableLiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public LiveData<List<OfficeOrderModel>> getListLiveData() {
        return listLiveData;
    }

    public void uploadSo(JSONObject object) {
        Call<UploadResponse> callUploadLogs = retrofitApi.uploadSo(object);
        callUploadLogs.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                UploadResponse uploadResponse = response.body();
                if (uploadResponse.code == 200) {
                    Log.e("Message", response.body().response);
                    mutableUploadError.setValue(uploadResponse.response);
                    new DeleteAllAsnycTask().execute();
                    return;
                }
                mutableUploadError.setValue("Something went wrong, please contact system administrator");
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    mutableUploadError.setValue("No network connection");
                } else {
                    mutableUploadError.setValue(t.getMessage());
                }


            }
        });

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

    class DeleteAllAsnycTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            officeOrderDao.deleteAllOfficerOrder();
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
