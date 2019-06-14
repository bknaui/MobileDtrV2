package com.dohro7.mobiledtrv2.broadcastreceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    private MutableLiveData<Boolean> mutableDownloadCompleted = new MutableLiveData<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            mutableDownloadCompleted.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getMutableDownloadCompleted(){
        return mutableDownloadCompleted;
    }
}
