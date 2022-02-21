package com.azubal.go4lunch.ui.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.azubal.go4lunch.workerManager.ReminderRestaurantWorker;
import com.azubal.go4lunch.workerManager.RestaurantChosenSetNullWorker;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    UserViewModel userViewModel;
    TextView textViewUserEmail;
    TextView textViewUserName;
    ImageView imageViewUserProfile;
    NavController navController;
    int delay;
    int delay1;

    @SuppressLint("LongLogTag")
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
        setDelayForTwoWorkManager();
    }

    public void setDelayForTwoWorkManager(){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),12,0);
        Calendar calendar2 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),17,0);
        long currentTime = calendar.getTime().getTime();
        long customTimeReminderNotification = calendar1.getTime().getTime();
        long customTimeRestoChosenSetNull = calendar2.getTime().getTime();
        Log.e("currentTime", String.valueOf(currentTime));
        Log.e("customTimeReminder", String.valueOf(customTimeReminderNotification));
        Log.e("customTimeResto", String.valueOf(customTimeRestoChosenSetNull));

        int currentTimeInt = (int) currentTime;
        int customTimeReminderNotificationInt = (int)  customTimeReminderNotification;
        int customTimeRestoChosenSetNullInt = (int) customTimeRestoChosenSetNull;
        Log.e("currentTimeInt", String.valueOf(currentTimeInt));
        Log.e("reminderNotificationInt", String.valueOf(customTimeReminderNotificationInt));
        Log.e("restoChosenSetNullInt", String.valueOf(customTimeRestoChosenSetNullInt));

        if (currentTimeInt < customTimeReminderNotificationInt) {

            delay = customTimeReminderNotificationInt - currentTimeInt;
            Log.e("delay", String.valueOf(delay));

            userViewModel.getUserData().observe(this, user -> {
                if(user.getRestaurantChosenAt12PM() != null){

                    userViewModel.getAllUsersPickForThisRestaurant(user.getRestaurantChosenAt12PM()).observe(this, users -> {
                        List<String> usernames = new ArrayList<>();

                        for (User user1 : users){
                            usernames.add(user1.getUsername());
                        }

                        String[] array = usernames.toArray(new String[0]);

                        Data restaurantChosen = new Data.Builder()
                                .putString("restaurantId",user.getRestaurantChosenAt12PM().getId())
                                .putString("restaurantName",user.getRestaurantChosenAt12PM().getName())
                                .putString("restaurantAddress",user.getRestaurantChosenAt12PM().getFormatted_address())
                                .putStringArray("restaurantListWorkmates",array)
                                .build();
                        scheduleNotification(delay,restaurantChosen);

                    });






                }
            });

        }

        if(currentTimeInt < customTimeRestoChosenSetNullInt ){
            delay1 = customTimeRestoChosenSetNullInt - currentTimeInt;
            Log.e("delay1", String.valueOf(delay1));
            setRestaurantChosenNull(delay1);
        }
    }



    public void setRestaurantChosenNull(int delay ){
        WorkManager workManager = WorkManager.getInstance(this);
        OneTimeWorkRequest restaurantChosenSetNullWork = new OneTimeWorkRequest.Builder(RestaurantChosenSetNullWorker.class).setInitialDelay(delay,TimeUnit.MILLISECONDS).build();
        workManager.enqueue(restaurantChosenSetNullWork);
    }

    public void scheduleNotification(int delay , Data data){
        WorkManager workManager = WorkManager.getInstance(this);
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(ReminderRestaurantWorker.class).setInitialDelay(delay,TimeUnit.MILLISECONDS).setInputData(data).build();
        workManager.enqueue(notificationWork);
    }

    private void setUpViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void setUpNavHostFragmentAndController(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    private void setUpAppBarAndConfigure(){
        setSupportActionBar(binding.toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapView, R.id.listView, R.id.workmates, R.id.yourLunch , R.id.settings , R.id.logout).setOpenableLayout(binding.drawerLayout).build();
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
        userViewModel.getUserData().observe(this, user -> {
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
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if(navDestination.getId() == R.id.yourLunch){
                Log.e("","");
                userViewModel.getUserData().observe(this, user -> {
                    if(user.getRestaurantChosenAt12PM() == null){
                        navController.popBackStack();
                        navController.navigate(R.id.mapView);
                    }
                });
            }
        });

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