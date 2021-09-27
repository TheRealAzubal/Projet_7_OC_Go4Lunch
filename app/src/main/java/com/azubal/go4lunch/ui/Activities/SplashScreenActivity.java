package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.azubal.go4lunch.viewmodels.AuthAppViewModel;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthAppViewModel authAppViewModel = new ViewModelProvider(this).get(AuthAppViewModel.class);

                 authAppViewModel.getUserLiveData().observe(this, firebaseUser -> {
                     if(firebaseUser != null) {
                         startActivityMain();
                     }else {
                         startActivityLogin();
                     }
                     finish();
                 });
    }

    private void startActivityLogin(){
        Intent LoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(LoginActivity);
    }

    private void startActivityMain(){
        Intent MainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(MainActivity);
    }

}