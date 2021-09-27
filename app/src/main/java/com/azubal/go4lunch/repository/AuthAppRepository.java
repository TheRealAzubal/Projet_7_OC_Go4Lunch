package com.azubal.go4lunch.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppRepository {
    Application application;
    FirebaseAuth firebaseAuth;
    AuthUI authUI;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.authUI = AuthUI.getInstance();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        MutableLiveData<FirebaseUser> result = new MutableLiveData<>();
        result.postValue(firebaseAuth.getCurrentUser());
        return result;
    }

    public MutableLiveData<Boolean> isCurrentUserLoggedIn(){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        result.postValue(firebaseAuth.getCurrentUser() != null);
        return result;
    }

    public void signOut(){
        authUI.signOut(application.getApplicationContext());
    }

    public void deleteUser(){
        authUI.delete(application.getApplicationContext());
    }

}
