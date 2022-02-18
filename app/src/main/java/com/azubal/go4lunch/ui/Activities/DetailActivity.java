package com.azubal.go4lunch.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityDetailBinding;
import com.azubal.go4lunch.ui.Fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding activityDetailBinding;
    String restaurantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        restaurantId = getIntent().getExtras().getString("restaurant_id");
        Bundle bundle = new Bundle();
        bundle.putString("restaurant_id",restaurantId);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, DetailFragment.class, bundle)
                .commit();

    }


}