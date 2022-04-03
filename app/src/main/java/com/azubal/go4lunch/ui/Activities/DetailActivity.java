package com.azubal.go4lunch.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.azubal.go4lunch.R;
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

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setTitle(R.string.titleDetailActivity);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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