package com.dohro7.mobiledtrv2.repository.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dohro7.mobiledtrv2.model.CtoModel;

import java.util.List;

@Dao
public interface CtoDao {

    @Insert
    void insertCto(CtoModel ctoModel);

    @Query("SELECT * FROM cto_tbl")
    LiveData<List<CtoModel>> getCto();

    @Delete
    void deleteCto(CtoModel ctoModel);
}
