package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.model.ResponseBody;
import com.dohro7.mobiledtrv2.model.UploadResponse;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.repository.local.AppDatabase;
import com.dohro7.mobiledtrv2.repository.local.CtoDao;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CtoRepository {

    private CtoDao ctoDao;
    private LiveData<List<CtoModel>> listLiveData;
    private MutableLiveData<String> mutableUploadError = new MutableLiveData<>();
    private RetrofitApi retrofitApi;

    public CtoRepository(Context context) {
        this.ctoDao = AppDatabase.getInstance(context).ctoDao();
        this.listLiveData = ctoDao.getCto();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
    }

    public MutableLiveData<String> getMutableUploadError() {
        return mutableUploadError;
    }

    public LiveData<List<CtoModel>> getListLiveData() {
        return listLiveData;
    }

    public void uploadCto(JSONObject jsonObject) {
        Call<UploadResponse> uploadCall = retrofitApi.uploadCto(jsonObject);
        uploadCall.enqueue(new Callback<UploadResponse>() {
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

    class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ctoDao.deleteAllCto();
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
