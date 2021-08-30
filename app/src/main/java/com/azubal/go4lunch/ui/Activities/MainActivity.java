package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar toolbar;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        setContentView(view);
        setUpView();
        setUpNavHostFragmentAndNavController();
        setUpBottomNavigation();
        setUpDrawerNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void setBinding(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
    }

    private void setUpView(){
        toolbar = binding.topAppBar;
        drawerLayout = binding.drawerLayout;
        bottomNavigationView =binding.bttmNav;
        navView =binding.navView;
    }

    private void setUpNavHostFragmentAndNavController(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
    }

    public void setUpBottomNavigation(){
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void setUpDrawerNavigation(){
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(drawerLayout).build();
        NavigationUI.setupWithNavController(navView,navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
}