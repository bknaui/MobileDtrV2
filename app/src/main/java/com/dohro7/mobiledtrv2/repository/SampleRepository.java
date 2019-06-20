package com.dohro7.mobiledtrv2.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

public class SampleRepository {

    private MutableLiveData<String> currentText = new MutableLiveData<>();

    public SampleRepository() {
        currentText.setValue("Tidirt");
    }

    public MutableLiveData<String> getCurrentText() {
        return currentText;
    }

    SampleRepository sampleRepository = new SampleRepository();
}
