package com.azubal.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.repository.AuthAppRepository;


import java.util.List;

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

    public  MutableLiveData<List<Restaurant>> getListRestaurant(){
        return authAppRepository.getListRestaurant();
    }

    public  MutableLiveData<List<User>> getAllUsers(){
        return authAppRepository.getAllUsers();
    }


    public void updateListRestaurant(List<Restaurant> listRestaurant){ authAppRepository.updateListRestaurant(listRestaurant);}

    public  MutableLiveData<Boolean> isLikeRestaurant(Restaurant restaurant){
        return authAppRepository.isLikeRestaurant(restaurant);
    }

    public void addRestaurantLike(Restaurant restaurant){authAppRepository.addRestaurantLike(restaurant);}

    public void deleteRestaurantLike(Restaurant restaurant){authAppRepository.deleteRestaurantLike(restaurant);}

}
