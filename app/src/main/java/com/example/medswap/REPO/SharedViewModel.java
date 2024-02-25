package com.example.medswap.REPO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<File> imageFileLiveData = new MutableLiveData<>();

    public void setImageFile(File file) {
        imageFileLiveData.setValue(file);
    }

    public LiveData<File> getImageFile() {
        return imageFileLiveData;
    }
}

