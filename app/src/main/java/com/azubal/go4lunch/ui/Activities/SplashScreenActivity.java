package com.azubal.go4lunch.ui.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.azubal.go4lunch.viewmodels.AuthAppViewModel;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthAppViewModel authAppViewModel = new ViewModelProvider(this).get(AuthAppViewModel.class);

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