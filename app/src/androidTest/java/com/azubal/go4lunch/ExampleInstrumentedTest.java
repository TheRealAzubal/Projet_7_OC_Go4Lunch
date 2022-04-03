package com.azubal.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import android.app.Application;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.repository.RestaurantRepository;
import com.azubal.go4lunch.repository.UserRepository;
import com.azubal.go4lunch.utils.MathOperationUtils;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    Application application;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    UserRepository userRepository;

    RestaurantViewModel restaurantViewModel;
    UserViewModel userViewModel;

    User userTest;
    Restaurant restaurantTest;

    @Before
    public void setUp(){
        application = ApplicationProvider.getApplicationContext();
        MockitoAnnotations.openMocks(this);
        restaurantViewModel = new RestaurantViewModel(restaurantRepository);
        userViewModel = new UserViewModel(userRepository);

        userTest = new User(
                "lrDbihUzO8b6tKTykhttsyPgDC62",
                "Test",
                null,
                "test@gmail.com",
                null);


        restaurantTest = new Restaurant();



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

        restaurantViewModel.getListRestaurant().observeForever(restaurants -> assertEquals(restaurants.size(),2));

    }

    @Test
    public void getFavoritesListRestaurant() {

        LatLng latLng = new LatLng(37.422131, -122.084801);
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant1 = new Restaurant();
        restaurantList.add(restaurant1);

        MutableLiveData<List<Restaurant>> data = new MutableLiveData<>();
        data.postValue(restaurantList);
        when(restaurantRepository.getRestaurants(latLng, true)).thenReturn(data);
        restaurantViewModel.setLatLng(latLng);
        restaurantViewModel.getFavoritesListRestaurant().observeForever(restaurants -> assertEquals(restaurants.size(), 1));

    }

    @Test
    public void getRestaurantById(){

        String id = "ChIJ5V-QXKiwj4ARv5e-BSB9fiA";
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(id);
        MutableLiveData<Restaurant> data = new MutableLiveData<>();
        data.postValue(restaurant1);
        when(restaurantRepository.getRestaurantById(id)).thenReturn(data);

        restaurantViewModel.getRestaurantById(id).observeForever(restaurant -> assertEquals(restaurant.getId(),id));

    }

    @Test
    public void isCurrentUserLoggedIn(){
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        data.postValue(false);
        when(userRepository.isCurrentUserLoggedIn()).thenReturn(data);
        userViewModel.isCurrentUserLoggedIn().observeForever(aBoolean -> assertEquals(aBoolean,false));
    }

    @Test
    public void getUserData(){
        MutableLiveData<User> data = new MutableLiveData<>();
        data.postValue(userTest);

        when(userRepository.getUserData()).thenReturn(data);
        userViewModel.getUserData().observeForever(user1 -> assertEquals(user1,userTest));
    }

    @Test
    public void getAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(userTest);

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.postValue(userList);

        when(userRepository.getAllUsers()).thenReturn(data);

        userViewModel.getAllUsers().observeForever(users -> assertEquals(users.size(), 1));

    }

    @Test
    public void getPosition() {
        LatLng latLng = new LatLng(37.422131,-122.084801);
        restaurantViewModel.setLatLng(latLng);
        restaurantViewModel.getPosition().observeForever(latLng1 -> assertEquals(latLng1,latLng));
    }

    @Test
    public void getAllUsersPickForThisRestaurant() {
        String id = "ChIJ5V-QXKiwj4ARv5e-BSB9fiA";
        String userUid = "lrDbihUzO8b6tKTykhttsyPgDC62";
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);


        List<User> userList = new ArrayList<>();
        userList.add(userTest);

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.postValue(userList);

        when(userRepository.getAllUsersPickForThisRestaurant(restaurant)).thenReturn(data);

        userViewModel.getAllUsersPickForThisRestaurant(restaurant).observeForever(users -> assertEquals(users.get(0).getUid(),userUid));

    }

    @Test
    public void isPickRestaurant() {
        String id = "ChIJ5V-QXKiwj4ARv5e-BSB9fiA";
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);


        MutableLiveData<Boolean> data = new MutableLiveData<>();
        data.postValue(false);

        when(restaurantRepository.isPickRestaurant(restaurant)).thenReturn(data);

        restaurantViewModel.isPickRestaurant(restaurant).observeForever(aBoolean -> assertEquals(aBoolean,false));

    }

    @Test
    public void isLikeRestaurant() {
        String id = "ChIJ5V-QXKiwj4ARv5e-BSB9fiA";
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);


        MutableLiveData<Boolean> data = new MutableLiveData<>();
        data.postValue(false);

        when(restaurantRepository.isLikeRestaurant(restaurant)).thenReturn(data);

        restaurantViewModel.isLikeRestaurant(restaurant).observeForever(aBoolean -> assertEquals(aBoolean,false));

    }

    @Test
    public void getRestaurantsBySearchQuery() {
        String id = "ChIJ5V-QXKiwj4ARv5e-BSB9fiA";
        String name = "In-N-Out Burger";
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);

        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        MutableLiveData<List<Restaurant>> data = new MutableLiveData<>();
        data.postValue(restaurantList);

        when(restaurantRepository.getRestaurantsBySearchQuery(name,false)).thenReturn(data);

        restaurantViewModel.getRestaurantsBySearchQuery(name,false).observeForever(restaurants -> assertEquals(restaurants.get(0).getName(),name));

    }

    @Test
    public void getDistance(){
        LatLng latLng1 = new LatLng(48.88288288288288,2.3563697384440787);
        LatLng latLng2 = new LatLng(48.87990360000001,2.3575559);
        int distance = MathOperationUtils.getDistance(latLng1, latLng2);
        assertEquals(343, distance);
    }

    @Test
    public void getRating(){
        int rating = MathOperationUtils.getRating(2);
        assertEquals(1, rating);
    }
}