package com.dohro7.mobiledtrv2.repository.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dohro7.mobiledtrv2.model.CtoModel;
import com.dohro7.mobiledtrv2.model.LeaveModel;
import com.dohro7.mobiledtrv2.model.OfficeOrderModel;
import com.dohro7.mobiledtrv2.model.TimeLogModel;

@Database(entities = {TimeLogModel.class, OfficeOrderModel.class, LeaveModel.class, CtoModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TimeLogDao timeLogDao();

    public abstract OfficeOrderDao officeOrderDao();

    public abstract LeaveDao leaveDao();

    public abstract CtoDao ctoDao();


    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "mob_db").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
