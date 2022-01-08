package com.azubal.go4lunch.repository;

import android.app.Application;
import android.location.Location;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;





import com.azubal.go4lunch.models.ApiDetails.PlaceDetails;
import com.azubal.go4lunch.models.ApiNearbySearch.NearbyResult;
import com.azubal.go4lunch.models.Restaurant;
import static com.azubal.go4lunch.BuildConfig.MAPS_API_KEY;
import com.azubal.go4lunch.service.ApiInterface;
import com.azubal.go4lunch.service.ApiService;
import com.google.android.gms.maps.model.LatLng;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    Application application;
    private final ApiInterface myInterface;
    private final MutableLiveData<List<Restaurant>> listRestaurantApiFirst = new MutableLiveData<>();


    public RestaurantRepository(Application application) {
        this.application = application;
        myInterface = ApiService.getInterface();
    }

    public MutableLiveData<List<Restaurant>> getListRestaurantApiFirst(LatLng latLng) {
        String location = latLng.latitude + "," + latLng.longitude;
        Call<NearbyResult> listRestaurantApiNearBySearchResponseOut = myInterface.getApiNearBySearchResponse(location, "3000");
        Log.e("getListRestaurants","0");
        listRestaurantApiNearBySearchResponseOut.enqueue(new Callback<NearbyResult>() {

            @Override
            public void onResponse(@NonNull Call<NearbyResult> call, @NonNull Response<NearbyResult> response) {
                Log.e("getListRestaurants","4");
                if (response.body() != null) {
                    List<Restaurant> restaurants = new ArrayList<>();

                    for (int i = 0; i < response.body().getResults().size(); i++) {

                        String place_id = response.body().getResults().get(i).getPlaceId();

                        Location originLocation = new Location("");
                        originLocation.setLatitude(latLng.latitude);
                        originLocation.setLongitude(latLng.longitude);

                        Location targetLocation = new Location("");
                        targetLocation.setLatitude(response.body().getResults().get(i).getGeometry().getLocation().getLat());
                        targetLocation.setLongitude(response.body().getResults().get(i).getGeometry().getLocation().getLng());

                        float distanceInMeters = originLocation.distanceTo(targetLocation);

                        PlaceDetails resultDetail = getRestaurantDetails(place_id);

                        String photoUrl = null;

                        if (response.body().getResults().get(i).getPhotos().get(0).getPhotoReference() !=null) {

                                photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                                        "?maxwidth=400" +
                                        "&photo_reference=" + resultDetail.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + MAPS_API_KEY;
                                Log.i("photourl", photoUrl);

                        }

                        double rating = 0;


                        if (response.body().getResults().get(i).getRating() != null){
                           rating = response.body().getResults().get(i).getRating();
                        }


                        Restaurant restaurant = new Restaurant(
                                place_id,
                                response.body().getResults().get(i).getName(),
                                "",
                                rating,
                                "",
                                ""+Math.round(distanceInMeters)+"m",
                                resultDetail.getResult().getFormattedAddress(),
                                resultDetail.getResult().getFormattedPhoneNumber(),
                                response.body().getResults().get(i).getGeometry().getLocation().getLat(),
                                response.body().getResults().get(i).getGeometry().getLocation().getLng(),
                                resultDetail.getResult().getOpeningHours(),
                                photoUrl,
                                resultDetail.getResult().getWebsite()
                        );
                        restaurants.add(restaurant);
                    }

                    listRestaurantApiFirst.postValue(restaurants);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyResult> call, @NonNull Throwable t) {
                listRestaurantApiFirst.postValue(null);
                Log.e("onFailure","5"+t.getLocalizedMessage());
            }
        });

        return listRestaurantApiFirst;
    }

    public PlaceDetails getRestaurantDetails(String place_id) {
        Call<PlaceDetails> detailOut = myInterface.getApiDetailsResponse(place_id);

        try {
            return detailOut.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }





}
