package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.source.AppDatabase;
import com.dohro7.mobiledtrv2.repository.source.TimeLogDao;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DtrRepository {
    private LiveData<List<TimeLogModel>> listLiveData;
    private TimeLogDao timeLogDao;

    public DtrRepository(Context context) {
        timeLogDao = AppDatabase.getInstance(context).timeLogDao();
        listLiveData = timeLogDao.getAllLogs();
    }

    public LiveData<List<TimeLogModel>> getTimeLogs() {
        return listLiveData;
    }

    public void uploadLogs(JSONObject jsonObject) {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApi();
        Call<String> callUploadLogs = retrofitApi.uploadTimelogs(jsonObject);
        callUploadLogs.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Response", response.body()+"OKAY");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            Log.e("ERROR",t.getMessage());
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
