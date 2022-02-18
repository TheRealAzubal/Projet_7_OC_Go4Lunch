package com.azubal.go4lunch.ui.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.azubal.go4lunch.viewmodels.UserViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel authAppViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        authAppViewModel.isCurrentUserLoggedIn().observe(this, isConnected -> {
            if (isConnected) {
                startActivityMain();
            } else {
                startActivityLogin();
            }
            finish();
        });

    }

    private void startActivityLogin() {
        Intent LoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(LoginActivity);
    }

    private void startActivityMain() {
        Intent MainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(MainActivity);
    }

}