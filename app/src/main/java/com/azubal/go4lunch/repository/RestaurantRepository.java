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

import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.service.ApiInterface;
import com.azubal.go4lunch.service.ApiService;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {


    private final ApiInterface myInterface;
    FirebaseAuth firebaseAuth;

    private static final String COLLECTION_NAME = "users";
    private static final String COLLECTION_RESTAURANT = "listRestaurant";
    private static final String COLLECTION_RESTAURANT_LIKE = "restaurantLike";
    private static final String COLLECTION_LIST_USERS_PICK = "listUsersPick";

    public RestaurantRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        myInterface = ApiService.getInterface();
    }

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT);
    }

    private CollectionReference getRestaurantsLikeCollection(){
        String uid = this.firebaseAuth.getUid();
        assert uid != null;
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(uid).collection(COLLECTION_RESTAURANT_LIKE);
    }

    private CollectionReference getListUsersPickCollection(String restaurantId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT).document(restaurantId).collection(COLLECTION_LIST_USERS_PICK);
    }

    public MutableLiveData<List<Restaurant>> getRestaurants(LatLng latLng, Boolean listIsFavorite) {
        MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        if(listIsFavorite){
            getRestaurantsLikeCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
                result.postValue(queryDocumentSnapshots.toObjects(Restaurant.class));
            });

        }else {

            getRestaurantsCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Restaurant> restaurantListFirebase = queryDocumentSnapshots.toObjects(Restaurant.class);

            if(restaurantListFirebase.size() <= 0){



                    String location = latLng.latitude + "," + latLng.longitude;
                    Call<NearbyResult> listRestaurantApiNearBySearchResponseOut = myInterface.getApiNearBySearchResponse(location, "3000");
                    Log.e("getListRestaurants", "0");
                    listRestaurantApiNearBySearchResponseOut.enqueue(new Callback<NearbyResult>() {

                        @Override
                        public void onResponse(@NonNull Call<NearbyResult> call, @NonNull Response<NearbyResult> response) {
                            Log.e("getListRestaurants", "4");
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

                                    if(response.body().getResults().get(i).getPhotos() != null) {

                                        if (response.body().getResults().get(i).getPhotos().get(0).getPhotoReference() != null) {

                                            photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                                                    "?maxwidth=400" +
                                                    "&photo_reference=" + resultDetail.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + MAPS_API_KEY;
                                            Log.i("photourl", photoUrl);

                                        }

                                    }

                                    double rating = 0;


                                    if (response.body().getResults().get(i).getRating() != null) {
                                        rating = response.body().getResults().get(i).getRating();
                                    }


                                    Restaurant restaurant = new Restaurant(
                                            place_id,
                                            response.body().getResults().get(i).getName(),
                                            rating,
                                            "" + Math.round(distanceInMeters) + "m",
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
                                result.postValue(restaurants);

                                for (Restaurant restaurant : restaurants) {
                                    getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
                                }


                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<NearbyResult> call, @NonNull Throwable t) {
                            result.postValue(null);
                            Log.e("onFailure", "5" + t.getLocalizedMessage());
                        }
                    });

                }else{
                    result.postValue(restaurantListFirebase);
                }



            });

        }


        return result;
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

    public MutableLiveData<Boolean> isLikeRestaurant(Restaurant restaurant){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getRestaurantsLikeCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(Restaurant restaurant1 : queryDocumentSnapshots.toObjects(Restaurant.class)){
                if(restaurant1.getId().equals(restaurant.getId())){
                    result.postValue(true);
                }else {
                    result.postValue(false);
                }
            }
        });
        return  result;
    }

    public void addRestaurantLike(Restaurant restaurant){
        getRestaurantsLikeCollection().document(restaurant.getId()).set(restaurant);
        Log.i("addRestaurantLike","addRestaurantLike");
    }

    public void deleteRestaurantLike(Restaurant restaurant){

        Log.e("deleteRestaurantLike","deleteRestaurantLike");

        getRestaurantsLikeCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(Restaurant restaurant1 : queryDocumentSnapshots.toObjects(Restaurant.class)){
                if(restaurant1.getId().equals(restaurant.getId())){
                    getRestaurantsLikeCollection().document(restaurant1.getId()).delete();
                }
            }
        });

    }





    public MutableLiveData<Boolean> isPickRestaurant(Restaurant restaurant){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get().addOnSuccessListener(documentSnapshot -> {

            if(Objects.requireNonNull(documentSnapshot.toObject(User.class)).getRestaurantChosenAt12PM() != null) {

                if (Objects.requireNonNull(documentSnapshot.toObject(User.class)).getRestaurantChosenAt12PM().getId().equals(restaurant.getId())) {
                    result.postValue(true);
                } else {
                    result.postValue(false);
                }

            }else {
                result.postValue(false);
            }

        });
        return  result;
    }

    public MutableLiveData<Restaurant> getRestaurantById(String restaurantId){
        MutableLiveData<Restaurant> result = new MutableLiveData<>();
        getRestaurantsCollection().document(restaurantId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.toObject(Restaurant.class) !=null) {
                result.postValue(queryDocumentSnapshots.toObject(Restaurant.class));
            }
        });
        return  result;
    }

    public void addUserPickForRestaurant(Restaurant restaurant){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("restaurantChosenAt12PM", restaurant);
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            getListUsersPickCollection(restaurant.getId()).document(firebaseAuth.getCurrentUser().getUid()).set(Objects.requireNonNull(queryDocumentSnapshots.toObject(User.class)));
        });
    }

    public void deleteUserPickForRestaurant(Restaurant restaurant){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("restaurantChosenAt12PM", null);
        getListUsersPickCollection(restaurant.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(User user1 : queryDocumentSnapshots.toObjects(User.class)){
                if(user1.getUid().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())){
                    getListUsersPickCollection(restaurant.getId()).document(firebaseAuth.getCurrentUser().getUid()).delete();
                }
            }
        });

    }

    public MutableLiveData<List<Restaurant>> getRestaurantsBySearchQuery(String query , Boolean requestIsFavorites) {
        MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        if (!requestIsFavorites) {

                getRestaurantsCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Restaurant> restaurantListFirebase = queryDocumentSnapshots.toObjects(Restaurant.class);
                    List<Restaurant> restaurantListSearch = new ArrayList<>();
                    for (int i = 0; i < restaurantListFirebase.size(); i++) {
                        String nameRestaurant = restaurantListFirebase.get(i).getName();

                        if(query.length() == nameRestaurant.length()) {

                            if (query.equals(nameRestaurant)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }

                        }else if(query.length() > nameRestaurant.length()){

                            String querySplit = query.substring(0,nameRestaurant.length());

                            if (querySplit.equals(nameRestaurant)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }

                        }else if (query.length() < nameRestaurant.length()){

                            String nameRestaurantSplit = nameRestaurant.substring(0,query.length());

                            if (query.equals(nameRestaurantSplit)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }
                        }

                    }
                    result.postValue(restaurantListSearch);
                });
                return result;

        } else {

                getRestaurantsLikeCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Restaurant> restaurantListFirebase = queryDocumentSnapshots.toObjects(Restaurant.class);
                    List<Restaurant> restaurantListSearch = new ArrayList<>();

                    for (int i = 0; i < restaurantListFirebase.size(); i++) {
                        String nameRestaurant = restaurantListFirebase.get(i).getName();

                        if(query.length() == nameRestaurant.length()) {

                            if (query.equals(nameRestaurant)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }

                        }else if(query.length() > nameRestaurant.length()){

                            String querySplit = query.substring(0,nameRestaurant.length());

                            if (querySplit.equals(nameRestaurant)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }

                        }else if (query.length() < nameRestaurant.length()){

                            String nameRestaurantSplit = nameRestaurant.substring(0,query.length());

                            if (query.equals(nameRestaurantSplit)) {
                                restaurantListSearch.add(restaurantListFirebase.get(i));
                            }
                        }
                    }
                    result.postValue(restaurantListSearch);
                });
                return result;
        }

    }

}
