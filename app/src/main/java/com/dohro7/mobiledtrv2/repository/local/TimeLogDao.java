package com.dohro7.mobiledtrv2.repository.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dohro7.mobiledtrv2.model.TimeLogModel;

import java.util.List;

@Dao
public interface TimeLogDao {

    @Insert
    void insertLogs(TimeLogModel timeLogModel);

    @Query("SELECT * from time_log ORDER BY id ASC")
    LiveData<List<TimeLogModel>> getAllLogs();


}
