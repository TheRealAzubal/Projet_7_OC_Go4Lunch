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
import java.util.List;
import java.util.Objects;

public class UserRepository {
    Application application;
    FirebaseAuth firebaseAuth;
    AuthUI authUI;
    private static final String COLLECTION_NAME = "users";

    public UserRepository(Application application) {
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
            result.postValue(userList);
        });
        return  result;
    }

}
