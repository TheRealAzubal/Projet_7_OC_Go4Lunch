package com.azubal.go4lunch.workerManager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RestaurantChosenSetNullWorker extends Worker {
    private static final String COLLECTION_NAME = "users";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public RestaurantChosenSetNullWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        setRestaurantChosenNull();
        return Result.success();
    }

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void setRestaurantChosenNull(){
        getUsersCollection().document(Objects.requireNonNull(firebaseAuth.getUid())).update("restaurantChosenAt12PM", null);
    }
}
