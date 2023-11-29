package com.example.greenday.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<String>> humidityArray;
    private final MutableLiveData<List<String>> postTimeArray;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        humidityArray = new MutableLiveData<>();
        postTimeArray = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String data) {
        mText.setValue(data);
    }

    public MutableLiveData<List<String>> getHumidityArray() {
        return humidityArray;
    }

    public void setHumidityArray(List<String> arr){
        humidityArray.setValue(arr);
    }


    public MutableLiveData<List<String>> getPostTimeArray() {
        return postTimeArray;
    }

    public void setPostTimeArray(List<String> arr){
        postTimeArray.setValue(arr);
    }



}