package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.azubal.go4lunch.manager.UserManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    AppBarConfiguration appBarConfiguration;
    private UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapView, R.id.listView, R.id.workmates, R.id.yourLunch , R.id.settings , R.id.logout).setDrawerLayout(binding.drawerLayout).build();

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.navViewDrawer, navController);


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

    public void logOutUser(){
        userManager.signOut(this).addOnSuccessListener(aVoid -> {
             finish();
        });
    }
    public void deleteUserAccount(){
        new AlertDialog.Builder(this)
                .setMessage("Voulez vous vraiment supprimer votre compte")
                .setPositiveButton("Confirmer", (dialogInterface, i) ->
                        userManager.deleteUser(this)
                                .addOnSuccessListener(aVoid -> {
                                             finish();
                                        }
                                )
                )
                .setNegativeButton("Annuler", null)
                .show();

    }
}