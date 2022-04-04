package com.azubal.go4lunch.viewmodels;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.repository.UserRepository;


import java.util.List;

public class UserViewModel extends ViewModel {
    UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public UserViewModel(UserRepository userRepository){this.userRepository = userRepository;}

    public MutableLiveData<Boolean> isCurrentUserLoggedIn(){
        return userRepository.isCurrentUserLoggedIn();
    }

    public void signOut(Context context){
        userRepository.signOut(context);
    }

    public void deleteUser(Context context){
        userRepository.deleteUser(context);
    }

    public void createUser(){
        userRepository.createUser();
    }

    public  MutableLiveData<User> getUserData(){
        return userRepository.getUserData();
    }

    public  MutableLiveData<List<User>> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public  MutableLiveData<List<User>> getAllUsersPickForThisRestaurant(Restaurant restaurant,Boolean withoutCurrentUser){
        return userRepository.getAllUsersPickForThisRestaurant(restaurant,withoutCurrentUser);
    }

    public void setUserName(String userName){
        userRepository.setUsername(userName);
    }

    public void setUserEmail(String email){
        userRepository.setUserEmail(email);
    }

    public void setPhotoUrl(String photoUrl){userRepository.setPhotoUrl(photoUrl);}

}
