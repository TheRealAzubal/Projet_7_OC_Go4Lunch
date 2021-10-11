package com.azubal.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.repository.AuthAppRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;


import java.util.List;
import java.util.Map;

public class AuthAppViewModel extends AndroidViewModel {
    AuthAppRepository authAppRepository;

    public AuthAppViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
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

    public void createUser(){
        authAppRepository.createUser();
    }

    public  MutableLiveData<User> getUserData(){
        return authAppRepository.getUserData();
    }

    public void updateMap(Map<String, Restaurant> map){
        authAppRepository.updateMap(map);
    }

}
