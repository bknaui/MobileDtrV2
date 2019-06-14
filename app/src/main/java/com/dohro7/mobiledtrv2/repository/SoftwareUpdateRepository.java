package com.dohro7.mobiledtrv2.repository;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.model.SoftwareUpdateModel;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitApi;
import com.dohro7.mobiledtrv2.repository.remote.RetrofitClient;
import com.dohro7.mobiledtrv2.utility.SystemUtility;

import java.io.File;

public class SoftwareUpdateRepository {
    private MutableLiveData<SoftwareUpdateModel> mutableSoftwareModel;
    private MutableLiveData<Double> mutableDownloadPercentage;
    private RetrofitApi retrofitApi;
    private String versionName;
    private DownloadManager downloadManager;
    private long id;
    private Handler handler = new Handler();
    private Runnable downloadRunnable = new Runnable() {
        @Override
        public void run() {

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);

            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                double progress = 0.0;
                int sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int downloadedIndex = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                long size = c.getInt(sizeIndex);
                long downloaded = c.getInt(downloadedIndex);

                if (size != -1) progress = downloaded * 100.0 / size;
                mutableDownloadPercentage.setValue(progress);

                Log.e("Progress", progress + " "+downloadedIndex);
                if (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) != DownloadManager.STATUS_SUCCESSFUL) {
                    handler.postDelayed(this, 2000);
                }
            }


        }
    };

    public SoftwareUpdateRepository(Context context) {
        mutableSoftwareModel = new MutableLiveData<>();
        retrofitApi = RetrofitClient.getRetrofitApi(context);
        versionName = SystemUtility.getVersionName(context);
        mutableDownloadPercentage = new MutableLiveData<>();
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public MutableLiveData<Double> getMutableDownloadPercentage() {
        return mutableDownloadPercentage;
    }

    public MutableLiveData<SoftwareUpdateModel> getMutableSoftwareModel() {
        return mutableSoftwareModel;
    }

    public void downloadApkFromServer() {
        File file = new File(Environment.getExternalStorageDirectory(), "MobileDtr/dtr.apk");

        Uri apkUri = Uri.fromFile(file);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://192.168.100.17/dtr/public/apk/dtr.apk"));
        request.setTitle("Downloading dtr.apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(apkUri);
        id = downloadManager.enqueue(request);


        handler.postDelayed(downloadRunnable, 5000);

        Log.e("Downloading", file.getAbsolutePath());

    }

    public void checkSoftwareUpdate() {
        SoftwareUpdateModel softwareUpdateModel = new SoftwareUpdateModel("1.0.0", "1. Daily alarm notification every 12:45 AM\n\n2. More user friendly UI Design");
        mutableSoftwareModel.setValue(softwareUpdateModel);/*
        Call<SoftwareUpdateModel> softwareUpdateModelCall = retrofitApi.checkSoftwareUpdate();
        softwareUpdateModelCall.enqueue(new Callback<SoftwareUpdateModel>() {
            @Override
            public void onResponse(Call<SoftwareUpdateModel> call, Response<SoftwareUpdateModel> response) {
                mutableSoftwareModel.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SoftwareUpdateModel> call, Throwable t) {
                mutableSoftwareModel.setValue(null);

        });
        */

    }


}
