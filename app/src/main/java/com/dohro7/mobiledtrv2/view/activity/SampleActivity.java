package com.dohro7.mobiledtrv2.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dohro7.mobiledtrv2.R;
import com.dohro7.mobiledtrv2.viewmodel.SampleViewModel;

public class SampleActivity extends AppCompatActivity {

    TextView sampleText;

    SampleViewModel sampleViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);
        sampleText = findViewById(R.id.sample_text);
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        sampleViewModel.getCurrentText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sampleText.setText(s);
            }
        });
    }

}
