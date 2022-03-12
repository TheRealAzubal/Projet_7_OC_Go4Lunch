package com.azubal.go4lunch.repository;

import android.app.Application;
import android.content.Context;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    FirebaseAuth firebaseAuth;
    AuthUI authUI;
    private static final String COLLECTION_NAME = "users";
    private static final String COLLECTION_RESTAURANT = "listRestaurant";
    private static final String COLLECTION_LIST_USERS_PICK = "listUsersPick";

    public UserRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.authUI = AuthUI.getInstance();
    }

    public MutableLiveData<Boolean> isCurrentUserLoggedIn(){
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        result.postValue(firebaseAuth.getCurrentUser() != null);
        return result;
    }

    public void signOut(Context context){
        authUI.signOut(context);
    }

    public void deleteUser(Context context){
        String uid = this.firebaseAuth.getUid();
        if(uid != null){
            getUsersCollection().document(uid).delete();
        }
        authUI.delete(context);

        getRestaurantsCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {


            for(Restaurant restaurant : queryDocumentSnapshots.toObjects(Restaurant.class)){

                getListUsersPickCollection(restaurant.getId()).get().addOnSuccessListener(queryDocumentSnapshots1 -> {

                    for(User user : queryDocumentSnapshots1.toObjects(User.class)){

                        if(user.getUid().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())){

                            getListUsersPickCollection(restaurant.getId()).document(Objects.requireNonNull(uid)).delete();
                        }

                    }


                });




            }

        });



    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT);
    }

    private CollectionReference getListUsersPickCollection(String restaurantId){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT).document(restaurantId).collection(COLLECTION_LIST_USERS_PICK);
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

    public MutableLiveData<List<User>> getAllUsers(){
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        getUsersCollection().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            for (Iterator<User> iterator = userList.iterator(); iterator.hasNext();) {
                if (iterator.next().getUid().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                }
            }
            result.postValue(userList);
        });
        return  result;
    }

    public MutableLiveData<List<User>> getAllUsersPickForThisRestaurant(Restaurant restaurant){
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        getListUsersPickCollection(restaurant.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = queryDocumentSnapshots.toObjects(User.class);
            for (Iterator<User> iterator = userList.iterator(); iterator.hasNext();) {
                if (iterator.next().getUid().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                }
            }
            result.postValue(userList);
        });
        return  result;
    }

    public void setUserEmail(String email){
        firebaseAuth.getCurrentUser().updateEmail(email);
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("email",email);
    }

    public void setUsername(String username){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("username",username);
    }

    public void setPhotoUrl(String photoUrl){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("urlPicture",photoUrl);
    }

}
