package com.example.greenday.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<String>> co2Array;
    private final MutableLiveData<List<String>> postTimeArray;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        co2Array = new MutableLiveData<>();
        postTimeArray = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String data) {
        mText.setValue(data);
    }

    public MutableLiveData<List<String>> getCo2Array() {
        return co2Array;
    }

    public void setCo2Array(List<String> arr){
        co2Array.setValue(arr);
    }


    public MutableLiveData<List<String>> getPostTimeArray() {
        return postTimeArray;
    }

    public void setPostTimeArray(List<String> arr){
        postTimeArray.setValue(arr);
    }
}