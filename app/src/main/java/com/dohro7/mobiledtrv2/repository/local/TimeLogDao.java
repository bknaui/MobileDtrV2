package com.dohro7.mobiledtrv2.repository.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dohro7.mobiledtrv2.model.TimeLogModel;

import java.util.List;

@Dao
public interface TimeLogDao {

    @Insert
    void insertLogs(TimeLogModel timeLogModel);

    @Query("SELECT * from time_log ORDER BY id ASC")
    LiveData<List<TimeLogModel>> getAllLogs();

    @Query("SELECT * from time_log WHERE uploaded = 0 ORDER BY id ASC")
    LiveData<List<TimeLogModel>> getNotUploadedLogs();

    @Query("UPDATE time_log SET uploaded = 1")
    void uploadLogs();

}
