package com.example.medswap.REPO;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<File> imageFileLiveData = new MutableLiveData<>();

    public void setImageFile(File file) {
        // Ensure that this operation is performed on the main thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            imageFileLiveData.setValue(file);
        } else {
            // If not on the main thread, post the operation to the main thread
            imageFileLiveData.postValue(file);
        }
    }

    public LiveData<File> getImageFile() {
        return imageFileLiveData;
    }
}

