package com.azubal.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.repository.RestaurantRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    RestaurantRepository restaurantRepository;

    MutableLiveData<LatLng> positionLiveData = new MutableLiveData();

    LiveData<List<Restaurant>> listRestaurantLiveData = Transformations.switchMap(positionLiveData, position ->
            restaurantRepository.getListRestaurantApiFirst(position));

    public void setLatLng(LatLng latLng) {
        this.positionLiveData.setValue(latLng);
    }

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
    }


    public LiveData<List<Restaurant>> getListRestaurant() {
        return listRestaurantLiveData;
    }

    public MutableLiveData<LatLng> getPosition() {
        return positionLiveData;
    }




}
