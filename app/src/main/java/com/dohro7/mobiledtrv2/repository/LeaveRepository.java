package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.source.AppDatabase;
import com.dohro7.mobiledtrv2.repository.source.LeaveDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRepository {
    private LeaveDao leaveDao;
    private LiveData<List<LeaveModel>> listLiveData;

    public LeaveRepository(Context context) {
        this.leaveDao = AppDatabase.getInstance(context).leaveDao();
        this.listLiveData = leaveDao.getLeaves();
    }

    public LiveData<List<LeaveModel>> getListLiveData() {
        return listLiveData;
    }

    public void insertLeave(LeaveModel leaveModel) {
        new InsertAsyncTask().execute(leaveModel);
    }

    public void deleteLeave(LeaveModel leaveModel) {
        new DeleteAsyncTask().execute(leaveModel);
    }

    public void uploadLeaves()
    {
        RetrofitApi retrofitApi = RetrofitClient.getRetrofitApi();
        Call<String> stringCall = retrofitApi.uploadLeaves("","","");
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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

    class DeleteAsyncTask extends AsyncTask<LeaveModel, Void, Void> {

        @Override
        protected Void doInBackground(LeaveModel... leaveModels) {
            leaveDao.deleteLeave(leaveModels[0]);
            return null;
        }
    }
}
