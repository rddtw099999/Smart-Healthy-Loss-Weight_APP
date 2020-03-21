package com.minkai.lossweight_app.ui.Devices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DevicesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DevicesViewModel() {
        mText = new MutableLiveData<>();

    }


    public LiveData<String> getText() {
        return mText;
    }
}