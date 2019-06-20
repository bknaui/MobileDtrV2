package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.model.UploadResponse;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.LeaveDao;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRepository {
    private LeaveDao leaveDao;
    private LiveData<List<LeaveModel>> listLiveData;
    private RetrofitApi retrofitApi;
    private MutableLiveData<String> mutableUploadError = new MutableLiveData<>();

    public LeaveRepository(Context context) {
        this.leaveDao = AppDatabase.getInstance(context).leaveDao();
        this.listLiveData = leaveDao.getLeaves();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
    }

    public LiveData<List<LeaveModel>> getListLiveData() {
        return listLiveData;
    }

    public MutableLiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public void insertLeave(LeaveModel leaveModel) {
        new InsertAsyncTask().execute(leaveModel);
    }

    public void deleteLeave(LeaveModel leaveModel) {
        new DeleteAsyncTask().execute(leaveModel);
    }

    public void uploadLeaves(JSONObject jsonObject) {

        Call<UploadResponse> stringCall = retrofitApi.uploadLeaves(jsonObject);
        stringCall.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                UploadResponse uploadResponse = response.body();
                if (uploadResponse.code == 200) {
                    Log.e("Message", response.body().response);
                    mutableUploadError.setValue(uploadResponse.response);
                    new DeleteAllAsyncTask().execute();
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


    class InsertAsyncTask extends AsyncTask<LeaveModel, Void, Void> {

        @Override
        protected Void doInBackground(LeaveModel... leaveModels) {
            leaveDao.insertLeave(leaveModels[0]);
            return null;
        }
    }

    class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            leaveDao.deleteAllLeave();
            return null;
        }
    }

    class DeleteAsyncTask extends AsyncTask<LeaveModel, Void, Void> {

        @Override
        protected Void doInBackground(LeaveModel... leaveModels) {
            leaveDao.deleteLeave(leaveModels[0]);
            return null;
        }
    }
}
