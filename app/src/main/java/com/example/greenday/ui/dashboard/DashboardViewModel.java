package com.example.greenday.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<String>> tempArray;
    private final MutableLiveData<List<String>> postTimeArray;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        tempArray = new MutableLiveData<>();
        postTimeArray = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String data) {
        mText.setValue(data);
    }

    public MutableLiveData<List<String>> getTempArray() {
        return tempArray;
    }

    public void setTempArray(List<String> arr){
        tempArray.setValue(arr);
    }


    public MutableLiveData<List<String>> getPostTimeArray() {
        return postTimeArray;
    }

    public void setPostTimeArray(List<String> arr){
        postTimeArray.setValue(arr);
    }
}