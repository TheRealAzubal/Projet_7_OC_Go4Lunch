package com.azubal.go4lunch.ui.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.ActivityDetailBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.viewmodels.AuthAppViewModel;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    Restaurant restaurant;
    ActivityDetailBinding activityDetailBinding;
    AuthAppViewModel authAppViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());
        getRestaurant();
        authAppViewModel = new ViewModelProvider(this).get(AuthAppViewModel.class);
        restaurantNotNull();
    }

    public void getRestaurant(){
        restaurant = getIntent().getParcelableExtra("detail_restaurant");
    }

    public void restaurantNotNull(){
        if (restaurant != null) {
            Log.i("name",restaurant.getName());
            setUpRestaurantUI();
        }
    }

    public void setUpRestaurantUI(){

            activityDetailBinding.name.setText(restaurant.getName());
            activityDetailBinding.detailFormattedAddress.setText(restaurant.getFormatted_address());

            activityDetailBinding.phoneButton.setOnClickListener(view -> {
            requestPermissionPhone();
            });



            authAppViewModel.isLikeRestaurant(restaurant).observe(this, aBoolean -> {
                activityDetailBinding.likeButton.setSelected(aBoolean);
                Log.e("isLike",aBoolean.toString());
            });

            authAppViewModel.isPickRestaurant(restaurant).observe(this, aBoolean -> {
                activityDetailBinding.pickRestaurantButton.setSelected(aBoolean);
                Log.e("isPickRestaurant",aBoolean.toString());
            });

            activityDetailBinding.likeButton.setOnClickListener(view -> {
                activityDetailBinding.likeButton.setSelected(!activityDetailBinding.likeButton.isSelected());

                Log.e("likeButtonOnClick","aa");

                if (view.isSelected()) {
                    authAppViewModel.addRestaurantLike(restaurant);
                    //Handle selected state change
                } else {
                    authAppViewModel.deleteRestaurantLike(restaurant);
                }

            });


            activityDetailBinding.pickRestaurantButton.setOnClickListener(view -> {

                activityDetailBinding.pickRestaurantButton.setSelected(!activityDetailBinding.pickRestaurantButton.isSelected());

                if (view.isSelected()) {
                    authAppViewModel.setRestaurantChosen(restaurant);
                    //Handle selected state change
                } else {
                    authAppViewModel.setRestaurantChosenNull();
                    //Handle de-select state change
                }

            });

            activityDetailBinding.websiteButton.setOnClickListener(view -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite())));
            });


            Glide.with(this)
                    .load(restaurant.getPhotoUrl())
                    .placeholder(R.drawable.logo_go4lunch)
                    .centerCrop()
                    .into(activityDetailBinding.imageRestaurant);

            if (Math.round(restaurant.getRating()) ==0){

            }else if (Math.round(restaurant.getRating()) == 1){
                activityDetailBinding.firstStar.setVisibility(View.VISIBLE);
            }else if (Math.round(restaurant.getRating()) == 2){
                activityDetailBinding.firstStar.setVisibility(View.VISIBLE);
                activityDetailBinding.secondStar.setVisibility(View.VISIBLE);
            }else if (Math.round(restaurant.getRating()) == 3){
                activityDetailBinding.firstStar.setVisibility(View.VISIBLE);
                activityDetailBinding.secondStar.setVisibility(View.VISIBLE);
                activityDetailBinding.thirdStar.setVisibility(View.VISIBLE);
            }else if (Math.round(restaurant.getRating()) == 4){
                activityDetailBinding.firstStar.setVisibility(View.VISIBLE);
                activityDetailBinding.secondStar.setVisibility(View.VISIBLE);
                activityDetailBinding.thirdStar.setVisibility(View.VISIBLE);
                activityDetailBinding.fourStar.setVisibility(View.VISIBLE);
            }else if (Math.round(restaurant.getRating()) == 5){
                activityDetailBinding.firstStar.setVisibility(View.VISIBLE);
                activityDetailBinding.secondStar.setVisibility(View.VISIBLE);
                activityDetailBinding.thirdStar.setVisibility(View.VISIBLE);
                activityDetailBinding.fourStar.setVisibility(View.VISIBLE);
                activityDetailBinding.fiveStar.setVisibility(View.VISIBLE);
            }
    }

    private void requestPermissionPhone(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED ) {

            onRequestPermissionGranted();

        }else {
            requestPermissionLauncher.launch(
                    Manifest.permission.CALL_PHONE
            );
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

                if (isGranted) {

                    onRequestPermissionGranted();

                }  // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

            });

    public void  onRequestPermissionGranted(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+restaurant.getFormatted_phone_number()));
        startActivity(callIntent);
    }


}