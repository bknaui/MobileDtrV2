package com.dohro7.mobiledtrv2.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dohro7.mobiledtrv2.scheduler.DailyTaskScheduler;
import com.dohro7.mobiledtrv2.utility.BitmapDecoder;
import com.dohro7.mobiledtrv2.utility.DateTimeUtility;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.view.fragment.CTOFragment;
import com.dohro7.mobiledtrv2.view.fragment.DtrFragment;
import com.dohro7.mobiledtrv2.view.fragment.LeaveFragment;
import com.dohro7.mobiledtrv2.view.fragment.OfficeOrderFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private DtrFragment dtrFragment = new DtrFragment();
    private LeaveFragment leaveFragment = new LeaveFragment();
    private OfficeOrderFragment officeOrderFragment = new OfficeOrderFragment();
    private CTOFragment ctoFragment = new CTOFragment();
    private FragmentTransaction ft;


//Uncomment if you want to perform screen shot
    @Override
    public void onBackPressed() {
        File screenShotFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Screenshot");
        Bitmap bitmap = BitmapDecoder.screenShotView(this);
        String fileName = "screenshot_drawer.jpg";
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawerlayout);

        toolbar.setTitle("Daily Time Record");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);


        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.dashboard_content, dtrFragment).commit();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.addHeaderView(navigationHeader());

        new DailyTaskScheduler().setAlarm(this);

    }

    public View navigationHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.user_layout, null, false);
        TextView username = view.findViewById(R.id.user_name);
        //username.setText();
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.dtr_menu:
                toolbar.setTitle("Daily Time Record");
                ft.replace(R.id.dashboard_content, dtrFragment).commit();
                break;
            case R.id.leave_menu:
                toolbar.setTitle("Leave");
                ft.replace(R.id.dashboard_content, leaveFragment).commit();
                break;
            case R.id.so_menu:
                toolbar.setTitle("Office Order");
                ft.replace(R.id.dashboard_content, officeOrderFragment).commit();
                break;
            case R.id.cto_menu:
                toolbar.setTitle("Compensatory Time Off");
                ft.replace(R.id.dashboard_content, ctoFragment).commit();
                break;
            case R.id.software_update_menu:
                Intent softwareUpdateIntent = new Intent(this, SoftwareUpdateActivity.class);
                startActivity(softwareUpdateIntent);
                /*
                toolbar.setTitle("Software Update");
                ft.replace(R.id.dashboard_content, softwareUpdateFragment).commit();
                */
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
