package com.azubal.go4lunch.ui.Activities;



import static androidx.work.ExistingWorkPolicy.REPLACE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.azubal.go4lunch.workerManager.ReminderRestaurantWorker;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    UserViewModel authAppViewModel;
    TextView textViewUserEmail;
    TextView textViewUserName;
    ImageView imageViewUserProfile;
    NavController navController;

    final String NOTIFICATION_ID = "appName_notification_id";
    final String NOTIFICATION_CHANNEL = "appName_channel_01";
    final String NOTIFICATION_WORK = "appName_notification_work";

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


        final Calendar c = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("GMT+1");
        c.setTimeZone(tz);
        int currentTime = new LocalTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)).getMillisOfSecond();

        int customTimeNotification = new LocalTime(12, 0).getMillisOfSecond();
        int customTimeRemoveChosen = new LocalTime(23, 0).getMillisOfSecond();

        if(customTimeNotification > currentTime){
            int delay = customTimeNotification - currentTime;
            scheduleNotification(delay);
        }

    }

    public void scheduleNotification(int delay ){
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(ReminderRestaurantWorker.class).setInitialDelay(delay, TimeUnit.MILLISECONDS).build();
        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
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