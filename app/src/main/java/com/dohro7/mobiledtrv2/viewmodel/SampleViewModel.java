package com.dohro7.mobiledtrv2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dohro7.mobiledtrv2.repository.SampleRepository;

public class SampleViewModel extends ViewModel {

    private LiveData<String> currentText;
    private SampleRepository sampleRepository;

    //repository = crud operations, viewmodel-use crud operations fr repository

    public SampleViewModel(){
        sampleRepository = new SampleRepository();
        currentText = sampleRepository.getCurrentText();
    }

    public LiveData<String> getCurrentText(){
        return currentText;
    }
}
