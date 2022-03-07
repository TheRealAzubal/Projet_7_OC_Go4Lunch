package com.azubal.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.repository.RestaurantRepository;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    Observer<List<Restaurant>> observer;

    @Mock
    RestaurantRepository restaurantRepository;

    RestaurantViewModel restaurantViewModel;
    FirebaseAuth firebaseAuth;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        restaurantViewModel = new RestaurantViewModel(restaurantRepository);
        firebaseAuth =FirebaseAuth.getInstance();
    }

    @Test
    public void getListRestaurant() {
        LatLng latLng = new LatLng(37.422131,-122.084801);
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Name");
        restaurantList.add(restaurant1);
        restaurantList.add(restaurant1);

        MutableLiveData<List<Restaurant>> data = new MutableLiveData<>();
        data.postValue(restaurantList);

        when(restaurantRepository.getRestaurants(latLng,false)).thenReturn(data);
        restaurantViewModel.setLatLng(latLng);

        restaurantViewModel.getListRestaurant().observeForever(restaurants -> {
            assertEquals(restaurants.size(),2);
        });

    }



}