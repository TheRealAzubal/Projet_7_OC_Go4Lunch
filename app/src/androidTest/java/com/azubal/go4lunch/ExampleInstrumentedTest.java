package com.azubal.go4lunch;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    UserViewModel userViewModel;
    RestaurantViewModel restaurantViewModel;
    FirebaseAuth firebaseAuth;
    Context context;
    Application application;


    @Before
    public void setUp(){
        context = ApplicationProvider.getApplicationContext();
        application = ApplicationProvider.getApplicationContext();
        userViewModel = new UserViewModel(application);
        restaurantViewModel = new RestaurantViewModel();
        firebaseAuth =FirebaseAuth.getInstance();
    }

    @Test
    public void setLatLngAndGetPosition() throws InterruptedException {
        LatLng expected = new LatLng(100,100);
        restaurantViewModel.setLatLng(expected);
        LatLng actual = MutableLiveDataTestUtil.getValue(restaurantViewModel.getPosition());
        assertEquals(expected,actual);
    }

    @Test
    public void getListRestaurant() throws InterruptedException {
         List<Restaurant> restaurantList = LiveDataTestUtil.getValue(restaurantViewModel.getListRestaurant());
         assertEquals(20,restaurantList.size());
    }



}