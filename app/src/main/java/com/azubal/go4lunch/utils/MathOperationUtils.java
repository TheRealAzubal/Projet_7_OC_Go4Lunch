package com.azubal.go4lunch.utils;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class MathOperationUtils {

    public static Integer getDistance(@NonNull LatLng userLocation , @NonNull LatLng restaurantLocation){
        Location originLocation = new Location("");
        originLocation.setLatitude(userLocation.latitude);
        originLocation.setLongitude(userLocation.longitude);

        Location targetLocation = new Location("");
        targetLocation.setLatitude(restaurantLocation.latitude);
        targetLocation.setLongitude(restaurantLocation.longitude);
        float distanceInMeters = originLocation.distanceTo(targetLocation);

        return Math.round(distanceInMeters);
    }

    public static Integer getRating(double rating){
        long ratingLong = Math.round(rating);
        int ratingInt = (int) ratingLong;
        return Math.round((ratingInt*3)/5);
    }

}
