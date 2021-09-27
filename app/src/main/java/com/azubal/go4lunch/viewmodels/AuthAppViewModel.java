package com.azubal.go4lunch.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.repository.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppViewModel extends AndroidViewModel {
    AuthAppRepository authAppRepository;
    MutableLiveData<FirebaseUser> userLiveData;


    public AuthAppViewModel(@NonNull Application application) {
        super(application);

        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

}
