package com.azubal.go4lunch.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.azubal.go4lunch.models.ApiDetails.Period;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuthAppRepository {
    Application application;
    FirebaseAuth firebaseAuth;
    AuthUI authUI;
    private static final String COLLECTION_NAME = "users";
    private static final String COLLECTION_RESTAURANT = "restaurant";
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
        authUI.delete(application.getApplicationContext());
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document().collection(COLLECTION_RESTAURANT);
    }

    private CollectionReference getRestaurantLikeCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document().collection(COLLECTION_RESTAURANT_LIKE);
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
            Map<String, Restaurant> listRestaurant = new HashMap<>();

            User userToCreate = new User(uid, username, urlPicture, email,listRestaurant);

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

    public Task<Void> updateMap(Map<String, Restaurant> listRestaurant) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();
        return this.getUsersCollection().document(uid).update("listRestaurant", listRestaurant);
    }



    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.firebaseAuth.getUid();
        if(uid != null){
            getUsersCollection().document(uid).delete();
        }
    }

}
