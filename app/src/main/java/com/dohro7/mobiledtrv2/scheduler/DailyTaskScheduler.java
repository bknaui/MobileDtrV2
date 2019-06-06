package com.dohro7.mobiledtrv2.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dohro7.mobiledtrv2.utility.DateTimeUtility;

import java.util.Calendar;

public class DailyTaskScheduler {

    public void setAlarm(Context context) {
        if (Integer.parseInt(DateTimeUtility.getCurrentTime().split(":")[0]) <= 16 && Integer.parseInt(DateTimeUtility.getCurrentTime().split(":")[1]) <= 9) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 9);

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
