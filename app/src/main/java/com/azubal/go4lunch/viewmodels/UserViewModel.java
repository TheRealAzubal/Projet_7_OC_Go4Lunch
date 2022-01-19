package com.azubal.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.repository.UserRepository;


import java.util.List;

public class UserViewModel extends AndroidViewModel {
    com.azubal.go4lunch.repository.UserRepository authAppRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new com.azubal.go4lunch.repository.UserRepository(application);
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

    public  MutableLiveData<List<User>> getAllUsers(){
        return authAppRepository.getAllUsers();
    }

}
