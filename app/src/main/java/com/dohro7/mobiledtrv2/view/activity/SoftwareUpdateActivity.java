package com.dohro7.mobiledtrv2.view.activity;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.broadcastreceiver.DownloadBroadcastReceiver;
import com.dohro7.mobiledtrv2.model.SoftwareUpdateModel;
import com.dohro7.mobiledtrv2.utility.BitmapDecoder;
import com.dohro7.mobiledtrv2.viewmodel.SoftwareUpdateViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SoftwareUpdateActivity extends AppCompatActivity {
    private SoftwareUpdateViewModel softwareUpdateViewModel;
    private DownloadBroadcastReceiver downloadBroadcastReceiver = new DownloadBroadcastReceiver();
    private CardView updatedContainer;
    private CardView newContainer;
    private Group progressContainer;
    private ProgressBar downloadProgressbar;

    //Uncomment if you want to perform screen shot
    /*@Override
    public void onBackPressed() {
        File screenShotFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Screenshot");
        Bitmap bitmap = BitmapDecoder.screenShotView(this);
        String fileName = "screenshot_software_update_updated.jpg";
        File imageFolderFile = new File(screenShotFile, fileName);
        imageFolderFile.getParentFile().mkdirs();
        try {
            OutputStream fout = new FileOutputStream(imageFolderFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            Log.e("FNOE", e.getMessage());
        } catch (IOException e) {
            Log.e("IOE", e.getMessage());
        }
        Toast.makeText(this, "Screen captured", Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.software_version_update_layout);
        updatedContainer = findViewById(R.id.software_update_updated_container);
        newContainer = findViewById(R.id.software_update_new_container);
        progressContainer = findViewById(R.id.software_update_progress_container);
        downloadProgressbar = findViewById(R.id.downloading_progress);
        softwareUpdateViewModel = ViewModelProviders.of(this).get(SoftwareUpdateViewModel.class);
        softwareUpdateViewModel.getMutableUpdateModel().observe(this, new Observer<SoftwareUpdateModel>() {
            @Override
            public void onChanged(SoftwareUpdateModel softwareUpdateModel) {
                progressContainer.setVisibility(View.GONE);
                if (softwareUpdateModel == null) {
                    updatedContainer.setVisibility(View.VISIBLE);
                    newContainer.setVisibility(View.GONE);
                    return;
                }
                updatedContainer.setVisibility(View.GONE);
                newContainer.setVisibility(View.VISIBLE);
            }
        });
        downloadBroadcastReceiver.getMutableDownloadCompleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue()) {
                    Log.e("Complete", aBoolean.booleanValue() + "");
                }
            }
        });

        softwareUpdateViewModel.getMutableDownloadPercentage().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                downloadProgressbar.setProgress((int) aDouble.doubleValue());
            }
        });
        findViewById(R.id.software_details_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softwareUpdateViewModel.downloadApkFromServer();
            }
        });
        softwareUpdateViewModel.checkSotwareUpdate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(downloadBroadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadBroadcastReceiver);
    }

}
