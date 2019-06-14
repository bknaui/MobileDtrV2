package com.dohro7.mobiledtrv2.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;

public class SystemUtility {
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        return false;
    }

    public static boolean isTimeAutomatic(Context context) {
        boolean enabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1 &&
                    Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1) {
                enabled = true;
            }

        } else {
            if (android.provider.Settings.System.getInt(context.getContentResolver(), "auto_time", 0) == 1 &&
                    android.provider.Settings.System.getInt(context.getContentResolver(), "auto_time_zone", 0) == 1) {
                enabled = true;
            }
        }
        return enabled;
    }

    public static boolean isLocationEnabled(Context context) {
        boolean anyLocationProv = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        anyLocationProv |= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return anyLocationProv;

    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return e.getMessage();
        }

    }
}
