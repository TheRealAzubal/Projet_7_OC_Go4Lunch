package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.azubal.go4lunch.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpNavigation();
    }

    public void setUpNavigation(){
        bottomNavigationView =findViewById(R.id.bttm_nav);
        NavHostFragment navHostFragment =       (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }
}