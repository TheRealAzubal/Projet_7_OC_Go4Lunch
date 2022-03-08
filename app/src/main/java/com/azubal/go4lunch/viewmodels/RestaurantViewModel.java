package com.azubal.go4lunch.viewmodels;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.repository.RestaurantRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantViewModel extends ViewModel {
    RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public RestaurantViewModel(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    MutableLiveData<LatLng> positionLiveData = new MutableLiveData();
    public void setLatLng(LatLng latLng) {
        this.positionLiveData.setValue(latLng);
    }
    public MutableLiveData<LatLng> getPosition() {
        return positionLiveData;
    }

    LiveData<List<Restaurant>> listRestaurantLiveData = Transformations.switchMap(positionLiveData, position ->
            restaurantRepository.getRestaurants(position,false));

    LiveData<List<Restaurant>> listRestaurantLiveDataFavorites = Transformations.switchMap(positionLiveData, position ->
            restaurantRepository.getRestaurants(position,true));


    public  LiveData<List<Restaurant>> getListRestaurant(){
        return listRestaurantLiveData;
    }

    public  LiveData<List<Restaurant>> getFavoritesListRestaurant(){
        return listRestaurantLiveDataFavorites;
    }

    public  MutableLiveData<Boolean> isLikeRestaurant(Restaurant restaurant){
        return restaurantRepository.isLikeRestaurant(restaurant);
    }

    public void addRestaurantLike(Restaurant restaurant){restaurantRepository.addRestaurantLike(restaurant);}

    public void deleteRestaurantLike(Restaurant restaurant){restaurantRepository.deleteRestaurantLike(restaurant);}

    public  MutableLiveData<Boolean> isPickRestaurant(Restaurant restaurant){
        return restaurantRepository.isPickRestaurant(restaurant);
    }
    public void addUserPickForRestaurant(Restaurant restaurant){restaurantRepository.addUserPickForRestaurant(restaurant);}

    public void deleteUserPickForRestaurant(Restaurant restaurant){restaurantRepository.deleteUserPickForRestaurant(restaurant);}


    public  MutableLiveData<Restaurant> getRestaurantById(String restaurantId){
        return restaurantRepository.getRestaurantById(restaurantId);
    }

    public  MutableLiveData<List<Restaurant>> getRestaurantsBySearchQuery(String query,Boolean requestIsFavorites){
        Log.e("queryVM",query);
        return restaurantRepository.getRestaurantsBySearchQuery(query,requestIsFavorites);
    }
}
