package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.Activities.MainActivity;
import com.azubal.go4lunch.ui.ListViewAdapter;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

public class ListViewFragment extends Fragment {

    View view;
    RestaurantViewModel restaurantViewModel;
    UserViewModel authAppViewModel;
    Boolean listIsFavorite;

    public ListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_view, container, false);

        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        authAppViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        listIsFavorite = requireArguments().getBoolean("isListFavorite");
        Log.e("listIsFavorite", String.valueOf(listIsFavorite));

        //restaurantViewModel.getPosition().observe(this, latLng -> {
            //restaurantViewModel.getListRestaurant(latLng,listIsFavorite).observe(this, this::setUpRecyclerView);
        //});

        if(listIsFavorite){
            restaurantViewModel.getFavoritesListRestaurant().observe(getViewLifecycleOwner(), this::setUpRecyclerView);
        }else {
            restaurantViewModel.getListRestaurant().observe(getViewLifecycleOwner(), this::setUpRecyclerView);
        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.top_app_bar, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView sv = new SearchView(Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });
    }

    public void setUpRecyclerView(List<Restaurant> restaurantList){
        RecyclerView rvRestaurants = view.findViewById(R.id.recycler_view_list_view);
        rvRestaurants.setAdapter(new ListViewAdapter(restaurantList,this.getContext()));
        rvRestaurants.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}