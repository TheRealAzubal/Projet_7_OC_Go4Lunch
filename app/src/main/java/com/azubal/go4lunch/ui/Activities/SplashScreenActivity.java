package com.azubal.go4lunch.ui.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.azubal.go4lunch.viewmodels.UserViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        startActivityInTermsOfUserIsConnected();
    }

    public void startActivityInTermsOfUserIsConnected(){
        userViewModel.isCurrentUserLoggedIn().observe(this, isConnected -> {
            if (isConnected) {
                startActivityMain();
            } else {
                startActivityLogin();
            }
            finish();
        });
    }

    public void setBinding(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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