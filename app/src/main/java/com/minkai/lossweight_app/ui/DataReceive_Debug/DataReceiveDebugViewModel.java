package com.minkai.lossweight_app.ui.DataReceive_Debug;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataReceiveDebugViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DataReceiveDebugViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Data Receive Page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}