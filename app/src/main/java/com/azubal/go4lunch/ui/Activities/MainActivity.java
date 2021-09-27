package com.azubal.go4lunch.ui.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.azubal.go4lunch.viewmodels.AuthAppViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    AuthAppViewModel authAppViewModel;
    TextView textViewUserEmail;
    TextView textViewUserName;
    ImageView imageViewUserProfile;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        setContentView(binding.getRoot());
        setUpViewModel();
        setUpNavHostFragmentAndController();
        setUpAppBarAndConfigure();
        configureNavDrawerAndNavBottom();
        setUpViewHeader();
        updateUserData();
    }

    private void setUpViewModel(){
        authAppViewModel = new ViewModelProvider(this).get(AuthAppViewModel.class);
    }

    private void setUpNavHostFragmentAndController(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    private void setUpAppBarAndConfigure(){
        setSupportActionBar(binding.toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapView, R.id.listView, R.id.workmates, R.id.yourLunch , R.id.settings , R.id.logout).setDrawerLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private void setUpViewHeader(){
        View headView = binding.navViewDrawer.getHeaderView(0);
        textViewUserEmail = headView.findViewById(R.id.textViewEmail);
        textViewUserName = headView.findViewById(R.id.textViewUserName);
        imageViewUserProfile = headView.findViewById(R.id.imageView);
    }

    private void configureNavDrawerAndNavBottom(){
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.navViewDrawer, navController);
    }

    private void updateUserData(){
        authAppViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser!= null) {
                String email = firebaseUser.getEmail();
                String name = firebaseUser.getDisplayName();
                Uri photoUrl = firebaseUser.getPhotoUrl();
                if (photoUrl != null){
                    imageViewUserProfile.setImageURI(photoUrl);
                }
                textViewUserEmail.setText(email);
                textViewUserName.setText(name);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setBinding(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    public void finishMainActivity(){
        finish();
    }

    public void startActivityLogin(){
        Intent LoginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(LoginActivity);
    }
}