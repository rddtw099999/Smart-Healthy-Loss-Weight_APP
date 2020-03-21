package com.minkai.lossweight_app.ui.RealTimePlot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RealtimePlotViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RealtimePlotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This Realtime plot page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}