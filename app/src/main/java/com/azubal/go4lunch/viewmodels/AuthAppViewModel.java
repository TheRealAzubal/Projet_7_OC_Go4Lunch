package com.azubal.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.repository.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppViewModel extends AndroidViewModel {
    AuthAppRepository authAppRepository;

    public AuthAppViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return authAppRepository.getUserLiveData();
    }

    public MutableLiveData<Boolean> isCurrentUserLoggedIn(){
        return authAppRepository.isCurrentUserLoggedIn();
    }

    public void signOut(){
        authAppRepository.signOut();
    }

    public void deleteUser(){
        authAppRepository.deleteUser();
    }


}
