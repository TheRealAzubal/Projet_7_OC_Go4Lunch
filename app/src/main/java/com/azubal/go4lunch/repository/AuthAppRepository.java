package com.azubal.go4lunch.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthAppRepository {
    Application application;
    FirebaseAuth firebaseAuth;
    AuthUI authUI;
    private static final String COLLECTION_NAME = "users";
    private static final String COLLECTION_RESTAURANT = "listRestaurant";
    private static final String COLLECTION_RESTAURANT_LIKE = "restaurantLike";
    public AuthAppRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.authUI = AuthUI.getInstance();
    }

    public MutableLiveData<Boolean> isCurrentUserLoggedIn(){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        result.postValue(firebaseAuth.getCurrentUser() != null);
        return result;
    }

    public void signOut(){
        authUI.signOut(application.getApplicationContext());
    }

    public void deleteUser(){
        String uid = this.firebaseAuth.getUid();
        if(uid != null){
            getUsersCollection().document(uid).delete();
        }

        authUI.delete(application.getApplicationContext());

    }

    // Get the Collection Reference
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

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.i("createUser","A");
        if(user != null){
            Log.i("createUser","B");
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            String email = user.getEmail();

            User userToCreate = new User(uid, username, urlPicture, email, null);

            getUsersCollection().document(uid).set(userToCreate);

        }
    }

    public MutableLiveData<User> getUserData(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();
        MutableLiveData<User> result = new MutableLiveData<>();
        DocumentReference docRef = getUsersCollection().document(uid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user1 = documentSnapshot.toObject(User.class);
            result.postValue(user1);
        });
        return result;
    }




    public void updateListRestaurant(List<Restaurant> listRestaurant){

        for (Restaurant restaurant : listRestaurant) {
            getRestaurantsCollection().document(restaurant.getId()).set(restaurant);
        }

    }

    public MutableLiveData<List<Restaurant>> getListRestaurant(){
        MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();
        getRestaurantsCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Restaurant> restaurantList = queryDocumentSnapshots.toObjects(Restaurant.class);
            result.postValue(restaurantList);
        });
        return  result;
    }

    public MutableLiveData<List<User>> getAllUsers(){
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        getUsersCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            result.postValue(userList);
        });
        return  result;
    }

    public MutableLiveData<Boolean> isLikeRestaurant(Restaurant restaurant){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getRestaurantsLikeCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(Restaurant restaurant1 : queryDocumentSnapshots.toObjects(Restaurant.class)){
                if(restaurant1.getId().equals(restaurant.getId())){
                    //getRestaurantsLikeCollection().document(restaurant1.getId()).delete();
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

    public void setRestaurantChosen(Restaurant restaurant){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("restaurantChosenAt12PM", restaurant);
    }

    public void setRestaurantChosenNull(){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("restaurantChosenAt12PM", null);
    }


}
