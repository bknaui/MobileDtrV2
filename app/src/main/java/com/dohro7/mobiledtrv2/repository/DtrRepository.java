package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.source.AppDatabase;
import com.dohro7.mobiledtrv2.repository.source.TimeLogDao;

import java.util.List;

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

    public void insertTimeLog(TimeLogModel timeLogModel) {
        new InsertAsyncTask().execute(timeLogModel);
    }


    class InsertAsyncTask extends AsyncTask<TimeLogModel, Void, Void> {

        @Override
        protected Void doInBackground(TimeLogModel... timeLogModels) {
            timeLogDao.insertLogs(timeLogModels[0]);
            return null;
        }
    }

}
