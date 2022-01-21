package com.azubal.go4lunch.ui.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    UserViewModel authAppViewModel;
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
        authAppViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
        authAppViewModel.getUserData().observe(this, user -> {

            String email = user.getEmail();
            Log.i("userEmail",email);
            String name = user.getUsername();
            String photoUrl = user.getUrlPicture();
            if (photoUrl != null){
                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.outline_block_24)
                        .into(imageViewUserProfile);
            }
            textViewUserEmail.setText(email);
            textViewUserName.setText(name);

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

    public void launchDetailRestaurant(Restaurant restaurant, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("restaurant_id", restaurant.getId());
        context.startActivity(intent);
    }

}