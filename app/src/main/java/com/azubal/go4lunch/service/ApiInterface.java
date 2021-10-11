package com.azubal.go4lunch.service;


import static com.azubal.go4lunch.BuildConfig.MAPS_API_KEY;

import com.azubal.go4lunch.models.ApiDetails.PlaceDetails;
import com.azubal.go4lunch.models.ApiNearbySearch.NearbyResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("maps/api/place/nearbysearch/json?type=restaurant&key=" + MAPS_API_KEY)
    Call<NearbyResult> getApiNearBySearchResponse(@Query("location") String location , @Query("radius") String radius );

    @GET("maps/api/place/details/json?key=" + MAPS_API_KEY)
    Call<PlaceDetails> getApiDetailsResponse(@Query("place_id") String place_id );

}
