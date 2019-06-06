package com.dohro7.mobiledtrv2.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.lifecycle.LiveData;

import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.repository.source.AppDatabase;
import com.dohro7.mobiledtrv2.repository.source.TimeLogDao;

import java.io.File;
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
