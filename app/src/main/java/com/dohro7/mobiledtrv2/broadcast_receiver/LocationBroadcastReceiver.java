package com.dohro7.mobiledtrv2.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.model.LocationIdentifier;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;
import com.dohro7.mobiledtrv2.utility.SystemUtility;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    private LocationIdentifier locationIdentifier = new LocationIdentifier();
    private MutableLiveData<LocationIdentifier> mutableLiveDataLocation;

    public MutableLiveData<LocationIdentifier> getMutableLiveDataLocation() {
        return mutableLiveDataLocation;
    }

    public LocationBroadcastReceiver(Context context) {
        mutableLiveDataLocation = new MutableLiveData<>();
        updateLocationIdentifier(context);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        updateLocationIdentifier(context);
    }

    public void updateLocationIdentifier(Context context) {
        if (SystemUtility.isLocationEnabled(context)) {
            locationIdentifier.colorResource = R.color.location_callibrating;
            locationIdentifier.message = "Location/GPS callibrating";
            locationIdentifier.visible = View.VISIBLE;
            locationIdentifier.date = DateTimeUtility.getCurrentDate();
            locationIdentifier.time = DateTimeUtility.getCurrentTime();
        } else {
            locationIdentifier.colorResource = R.color.gps_disabled;
            locationIdentifier.message = "GPS is not enabled";
            locationIdentifier.visible = View.GONE;
        }
        mutableLiveDataLocation.setValue(locationIdentifier);
    }

}
