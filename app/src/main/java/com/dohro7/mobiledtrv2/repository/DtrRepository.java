package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.TimeLogDao;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DtrRepository {
    private LiveData<List<TimeLogModel>> listLiveData;
    private MutableLiveData<String> mutableUploadError;
    private TimeLogDao timeLogDao;
    private RetrofitApi retrofitApi;

    public DtrRepository(Context context) {
        timeLogDao = AppDatabase.getInstance(context).timeLogDao();
        listLiveData = timeLogDao.getAllLogs();
        mutableUploadError = new MutableLiveData<>();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
    }

    public MutableLiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public LiveData<List<TimeLogModel>> getTimeLogs() {
        return listLiveData;
    }

    public void uploadLogs(JSONObject jsonObject) {

        Call<String> callUploadLogs = retrofitApi.uploadTimelogs(jsonObject);
        callUploadLogs.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Response", response.body() + "OKAY");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof IOException) {
                    mutableUploadError.setValue("No network connection");
                } else {
                    mutableUploadError.setValue(t.getMessage());
                }

            }
        });

    }

    public void insertTimeLog(TimeLogModel timeLogModel) {
        new InsertAsyncTask().execute(timeLogModel);
    }

    public void deleteImages() {
        new DeleteAsyncTask().execute();
    }


    class InsertAsyncTask extends AsyncTask<TimeLogModel, Void, Void> {

        @Override
        protected Void doInBackground(TimeLogModel... timeLogModels) {
            timeLogDao.insertLogs(timeLogModels[0]);
            return null;
        }
    }

    class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStorageDirectory(), ".MobileDTRv2/Images");
            if (file.isDirectory()) {
                String[] files = file.list();
                for (int i = 0; i < files.length; i++) {
                    new File(file, files[i]).delete();
                }
            }
            return null;
        }
    }

}
