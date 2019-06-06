package com.dohro7.mobiledtrv2.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.adapter.DtrAdapter;
import com.dohro7.mobiledtrv2.broadcastreceiver.LocationBroadcastReceiver;
import com.dohro7.mobiledtrv2.model.LocationIdentifier;
import com.dohro7.mobiledtrv2.model.TimeLogModel;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;
import com.dohro7.mobiledtrv2.viewmodel.DtrViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DtrFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, ResultCallback, GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView recyclerView;
    private TextView txtLocationStatus;
    private ConstraintLayout locationStatusContainer;
    private TextView txtLocationDate;
    private TextView txtLocationTime;
    private DtrAdapter dtrAdapter;
    private MenuItem menuItem;
    private DtrViewModel dtrViewModel;


    private LocationBroadcastReceiver locationBroadcastReceiver;

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationSettingsRequest.Builder locationSettingsBuilder;
    private SettingsClient settingsClient;

    private String filePath = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dtrViewModel = ViewModelProviders.of(this).get(DtrViewModel.class);
        locationBroadcastReceiver = new LocationBroadcastReceiver(getContext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    Log.e("Location",locationResult.getLastLocation().getLatitude()+" "+locationResult.getLastLocation().getLongitude());
                    LocationIdentifier locationIdentifier = new LocationIdentifier();
                    locationIdentifier.visible = View.GONE;
                    locationIdentifier.message = "Location/GPS acquired";
                    locationIdentifier.colorResource = R.color.location_acquired;

                    locationBroadcastReceiver.getMutableLiveDataLocation().setValue(locationIdentifier);
                }

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dtr_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.dtr_recycler_view);
        txtLocationStatus = view.findViewById(R.id.location_status);
        txtLocationDate = view.findViewById(R.id.location_date);
        txtLocationTime = view.findViewById(R.id.location_time);
        locationStatusContainer = view.findViewById(R.id.location_status_container);

        dtrAdapter = new DtrAdapter();
        recyclerView.setAdapter(dtrAdapter);

        dtrViewModel.getMutableLiveDataList().observe(this, new Observer<List<TimeLogModel>>() {
            @Override
            public void onChanged(List<TimeLogModel> timeLogModels) {
                dtrAdapter.setList(timeLogModels);
            }
        });
        dtrViewModel.getMutableUndertime().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue()) {
                    Log.e("OK", "OK");
                    displayDialogUndertime();
                } else {
                    requestCameraAndStoragePermission();
                }
            }
        });

        dtrViewModel.getMutableTimeExists().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                displayAlreadyExistsDialog(s);
            }
        });
        locationBroadcastReceiver.getMutableLiveDataLocation().observe(this, new Observer<LocationIdentifier>() {
            @Override
            public void onChanged(LocationIdentifier locationIdentifier) {
                txtLocationDate.setText(locationIdentifier.date);
                txtLocationStatus.setText(locationIdentifier.message);
                txtLocationTime.setText(locationIdentifier.time);
                locationStatusContainer.setBackgroundResource(locationIdentifier.colorResource);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dtr_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem = menu.findItem(R.id.dtr_time);
        dtrViewModel.getLiveDataMenuTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                menuItem.setTitle(s);
            }
        });
        dtrViewModel.getLiveDataMenuTitle().setValue(dtrViewModel.getLiveDataMenuTitle().getValue());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //@TODO: Refactor put all business logic into ViewModel
            case R.id.dtr_time:
                dtrViewModel.timeValidate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayDialogUndertime() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_undertime_layout);

        dialog.findViewById(R.id.dialog_undertime_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_undertime_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraAndStoragePermission();
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void displayAlreadyExistsDialog(String message) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_already_timed);

        TextView dialogMessage = dialog.findViewById(R.id.dialog_already_message);
        dialogMessage.setText(message);

        dialog.findViewById(R.id.dialog_already_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void dispayActionCancelledDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_already_timed);


        dialog.findViewById(R.id.dialog_cancelled_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        getContext().registerReceiver(locationBroadcastReceiver, intentFilter);

        requestLocationPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                requestLocationPermission();
                return;
            case 1:
                requestCameraAndStoragePermission();
        }
    }

    public void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 0);
            Log.e("FusedApi", "Disabled");
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    private void requestCameraAndStoragePermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return;
        }
        String imageTimeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        File imageFolderFile = new File(getContext().getFilesDir(), "images");
        if (!imageFolderFile.exists()) imageFolderFile.mkdir();

        /*@Todo: not yet implemented*/
        File imageFile = new File(imageFolderFile, "0618_" + imageTimeStamp + ".png");
        filePath = imageFile.getAbsolutePath();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uriImage = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) ?
                FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", imageFile) :
                Uri.fromFile(imageFile);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(cameraIntent, 0);
        } else {
            Toast.makeText(getContext(), "No activity can handle this request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_timelog_layout);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    TextView dialogLogStatus = dialog.findViewById(R.id.dialog_timelog_status);
                    TextView dialogLogTime = dialog.findViewById(R.id.dialog_timelog_time);

                    String status = menuItem.getTitle().toString();
                    String currentTime = DateTimeUtility.getCurrentTime();
                    dialogLogStatus.setText(status);
                    dialogLogTime.setText(currentTime);

                /*
                @note: Uncomment to check if the picture really exists on this path

                ImageView dialogLogSelfie = dialog.findViewById(R.id.dialog_timelog_selfie);

                File file = new File(filePath);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    dialogLogSelfie.setImageBitmap(bitmap);
                }
                */

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;
                    dialog.getWindow().setAttributes(lp);
                    dialog.show();

                    TimeLogModel timeLogModel = new TimeLogModel();
                    timeLogModel.id = 0;
                    timeLogModel.date = DateTimeUtility.getCurrentDate();
                    timeLogModel.time = DateTimeUtility.getCurrentTime();
                    timeLogModel.status = status;
                    dtrViewModel.insertTimeLog(timeLogModel);
                    break;
            }
        } else {
            dispayActionCancelledDialog();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        getContext().unregisterReceiver(locationBroadcastReceiver);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationSettingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        locationSettingsBuilder.setAlwaysShow(true);

        locationSettingsRequest = locationSettingsBuilder.build();

        PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest);

        settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult((Activity) getContext(), 100);
                } catch (IntentSender.SendIntentException e) {
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
