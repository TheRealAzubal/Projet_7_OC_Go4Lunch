package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.azubal.go4lunch.manager.UserManager;

public class SplashScreenActivity extends AppCompatActivity {

    private final UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyIfIsCurrentUserLogged();
    }

    private void verifyIfIsCurrentUserLogged(){
        if(userManager.isCurrentUserLogged()) {
            startActivityMain();
        }else {
            startActivityLogin();
        }
        finish();
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