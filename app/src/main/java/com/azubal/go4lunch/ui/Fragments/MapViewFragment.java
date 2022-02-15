package com.azubal.go4lunch.ui.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentMapViewBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.Activities.DetailActivity;
import com.azubal.go4lunch.ui.Activities.MainActivity;
import com.azubal.go4lunch.utils.ToastUtil;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import java.util.Objects;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    View view;
    GoogleMap map;
    private FragmentMapViewBinding binding;
    MainActivity mainActivity;
    RestaurantViewModel restaurantViewModel;
    UserViewModel authAppViewModel;
    SupportMapFragment mapFragment;



    public MapViewFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        requireActivity().getMenuInflater().inflate(R.menu.top_app_bar, menu);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                restaurantViewModel.getListRestaurant().observe(getViewLifecycleOwner(), (Observer<List<Restaurant>>) restaurants -> {
                    setMarkers(restaurants);
                });
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setOnActionExpandListener(expandListener);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("mapviewonQueryTextSubmit",query);

                restaurantViewModel.getRestaurantsBySearchQuery(query,false).observe(getViewLifecycleOwner(), (Observer<List<Restaurant>>) restaurants -> {
                    Log.e("queryMapView",query);
                    for(Restaurant restaurant : restaurants){
                        Log.e("MapViewSearchResult",restaurant.getName()+" : "+restaurant.getFormatted_address());
                    }
                    map.clear();

                    setMarkers(restaurants);

                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        // Catch event on [x] button inside search view




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
        authAppViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
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



        restaurantViewModel.getPosition().observe(this, this::moveCamera);

        restaurantViewModel.getListRestaurant().observe(this, list -> {
            setMarkers(list);
            prepareDetailActivity(list);
        });

    }

    public void prepareDetailActivity(List<Restaurant> restaurantList){
        map.setOnInfoWindowClickListener(marker -> launchDetailRestaurant(restaurantList.get((int) marker.getZIndex()),requireContext()));
    }





    public void setMarkers(List<Restaurant> restaurantList){
        if(restaurantList != null) {

            for (int i = 0; i < restaurantList.size(); i++) {
                Restaurant restaurantCurrent = restaurantList.get(i);
                int numberRestaurantInList = i;


                LatLng latLng = new LatLng(restaurantCurrent.getLatitude(),restaurantCurrent.getLongitude());



                        authAppViewModel.getAllUsersPickForThisRestaurant(restaurantCurrent).observe(this, users -> {
                            if (users.size() > 0 ){
                                map.addMarker(new MarkerOptions().position(latLng).title(restaurantCurrent.getName()).zIndex(numberRestaurantInList).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant_green)));
                            }else{
                                map.addMarker(new MarkerOptions().position(latLng).title(restaurantCurrent.getName()).zIndex(numberRestaurantInList).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant_orange)));
                            }
                        });











            }
        }
    }

    public void moveCamera(LatLng latLng){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    public void launchDetailRestaurant(Restaurant restaurant, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("restaurant_id", restaurant.getId());
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