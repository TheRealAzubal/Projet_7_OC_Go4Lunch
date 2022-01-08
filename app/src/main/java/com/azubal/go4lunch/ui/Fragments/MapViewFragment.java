package com.azubal.go4lunch.ui.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.azubal.go4lunch.databinding.FragmentMapViewBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.Activities.DetailActivity;
import com.azubal.go4lunch.ui.Activities.MainActivity;
import com.azubal.go4lunch.viewmodels.AuthAppViewModel;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    View view;
    GoogleMap map;
    private FragmentMapViewBinding binding;
    MainActivity mainActivity;
    RestaurantViewModel restaurantViewModel;
    AuthAppViewModel authAppViewModel;
    SupportMapFragment mapFragment;



    public MapViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.map.getId());

        getMainActivity();



        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        authAppViewModel = new ViewModelProvider(requireActivity()).get(AuthAppViewModel.class);
        requestPermissionLocation();

        return view;
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }

    public void syncMap(){
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);

        authAppViewModel.getListRestaurant().observe(this, list -> {

            if (list != null){
                setMarkers(list);
                prepareDetailActivity(list);
            }else{
                restaurantViewModel.getListRestaurant().observe(this, restaurantList -> {
                    setMarkers(restaurantList);
                    prepareDetailActivity(restaurantList);
                    authAppViewModel.updateListRestaurant(restaurantList);
                });
            }

        });

        restaurantViewModel.getPosition().observe(this, this::moveCamera);
    }

    public void prepareDetailActivity(List<Restaurant> restaurantList){
        map.setOnInfoWindowClickListener(marker -> launchDetailRestaurant(restaurantList.get((int) marker.getZIndex()),requireContext()));
    }



    public void setMarkers(List<Restaurant> restaurantList){
        if(restaurantList != null) {

            for (int i = 0; i < restaurantList.size(); i++) {
                LatLng latLng = new LatLng(restaurantList.get(i).getLatitude(),restaurantList.get(i).getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title(restaurantList.get(i).getName()).zIndex(i));
            }
        }
    }

    public void moveCamera(LatLng latLng){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    public void launchDetailRestaurant(Restaurant restaurant, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("detail_restaurant", restaurant);
        context.startActivity(intent);
    }

    private void requestPermissionLocation(){
        Log.e("requestPermission","1");
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {

            onRequestPermissionGranted();

        }else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        }
    }



    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.e("requestLauncher","2");
                if (isGranted) {

                    onRequestPermissionGranted();

                } else {
                    Log.e("requestLauncher","3");
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    public void onRequestPermissionGranted(){
        Log.e("requestPermission","0");

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("myPosition", String.valueOf(myPosition));
            restaurantViewModel.setLatLng(myPosition);
            syncMap();
        });
    }

}