package com.dohro7.mobiledtrv2.repository.source;

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

    @Query("SELECT * from time_log ORDER BY date,time ASC")
    LiveData<List<TimeLogModel>> getAllLogs();

}
