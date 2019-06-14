package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.CtoDao;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CtoRepository {

    private CtoDao ctoDao;
    private LiveData<List<CtoModel>> listLiveData;
    private RetrofitApi retrofitApi;

    public CtoRepository(Context context) {
        this.ctoDao = AppDatabase.getInstance(context).ctoDao();
        this.listLiveData = ctoDao.getCto();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
    }

    public LiveData<List<CtoModel>> getListLiveData() {
        return listLiveData;
    }

    public void uploadCto(JSONObject jsonObject) {
        Call<String> uploadCall = retrofitApi.uploadCto(jsonObject);
        uploadCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {

                    return;
                }
                if (response.body().equalsIgnoreCase("1")) {
                    Log.e("Success","Success");
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    public void insertCto(CtoModel ctoModel) {
        new InsertAsyncTask().execute(ctoModel);
    }

    public void deleteCto(CtoModel ctoModel) {
        new DeleteAsyncTask().execute(ctoModel);
    }


    class InsertAsyncTask extends AsyncTask<CtoModel, Void, Void> {

        @Override
        protected Void doInBackground(CtoModel... ctoModels) {
            ctoDao.insertCto(ctoModels[0]);
            return null;
        }
    }

    class DeleteAsyncTask extends AsyncTask<CtoModel, Void, Void> {

        @Override
        protected Void doInBackground(CtoModel... ctoModels) {
            ctoDao.deleteCto(ctoModels[0]);
            return null;
        }
    }


}
