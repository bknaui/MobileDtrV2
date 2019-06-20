package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.model.UploadResponse;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.TimeLogDao;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DtrRepository {
    private LiveData<List<TimeLogModel>> listLiveData;
    private TimeLogDao timeLogDao;
    private RetrofitApi retrofitApi;
    private MutableLiveData<String> uploadMessage = new MutableLiveData<>();
    private Context context;

    public DtrRepository(Context context) {
        this.context = context;
        timeLogDao = AppDatabase.getInstance(context).timeLogDao();
        listLiveData = timeLogDao.getAllLogs();
        retrofitApi = RetrofitClient.getRetrofitApi(context);

    }

    public MutableLiveData<String> getUploadMessage() {
        return uploadMessage;
    }

    public LiveData<List<TimeLogModel>> getTimeLogs() {
        return listLiveData;
    }

    public void uploadLogs(JSONObject jsonObject) {

        Call<UploadResponse> callUploadLogs = retrofitApi.uploadTimelogs(jsonObject);
        callUploadLogs.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                UploadResponse uploadResponse = response.body();
                if (uploadResponse.code == 200) {
                    Log.e("Message", response.body().response);
                    uploadMessage.setValue(response.body().response);
                    new UploadAsyncTask().execute();
                    return;
                }
                uploadMessage.setValue("Something went wrong, please contact system administrator");
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                if (t instanceof IOException) {
                    uploadMessage.setValue("No network connection");
                } else {
                    uploadMessage.setValue(t.getMessage());
                }

            }
        });

    }


    public void insertTimeLog(TimeLogModel timeLogModel) {
        new InsertAsyncTask().execute(timeLogModel);
    }

    class UploadAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            timeLogDao.uploadLogs();
//            File screenshot = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Screenshot");
//            if (screenshot.isDirectory()) {
//                String[] files = screenshot.list();
//                for (int i = 0; i < files.length; i++) {
//                    new File(screenshot, files[i]).delete();
//                }
//            }
//
//            File timelog = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Timelogs");
//            if (timelog.isDirectory()) {
//                String[] files = timelog.list();
//                for (int i = 0; i < files.length; i++) {
//                    new File(timelog, files[i]).delete();
//                }
//            }
            return null;
        }
    }

    class InsertAsyncTask extends AsyncTask<TimeLogModel, Void, Void> {

        @Override
        protected Void doInBackground(TimeLogModel... timeLogModels) {
            timeLogDao.insertLogs(timeLogModels[0]);
            return null;
        }
    }
}
