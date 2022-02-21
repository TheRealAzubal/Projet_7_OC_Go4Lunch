package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.azubal.go4lunch.databinding.ActivityDetailBinding;
import com.azubal.go4lunch.ui.Fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding activityDetailBinding;
    String restaurantId;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding();
        setRestaurantIdAndBundle();
        setFragment();
    }

    public void setBinding(){
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());
    }

    public void setRestaurantIdAndBundle(){
        restaurantId = getIntent().getExtras().getString("restaurant_id");
        bundle = new Bundle();
        bundle.putString("restaurant_id",restaurantId);
    }

    public void setFragment(){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(activityDetailBinding.fragmentContainerView.getId(), DetailFragment.class, bundle)
                .commit();
    }

}