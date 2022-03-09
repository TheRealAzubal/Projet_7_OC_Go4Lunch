package com.azubal.go4lunch.ui.Fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentListViewBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.ListViewAdapter;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import java.util.List;

public class ListViewFragment extends Fragment {

    View view;
    FragmentListViewBinding fragmentListViewBinding;
    RestaurantViewModel restaurantViewModel;
    UserViewModel userViewModel;
    Boolean listIsFavorite;

    public ListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        requireActivity().getMenuInflater().inflate(R.menu.top_app_bar, menu);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);


        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getRestaurantListOrRestaurantListFavorites(listIsFavorite);
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
        final SearchView searchView = (SearchView) menuItem.getActionView();


        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("listviewonQueryTextSubmit",query);
                getRestaurantByQuery(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });






    }

    public void getRestaurantByQuery(String query){
        restaurantViewModel.getRestaurantsBySearchQuery(query,listIsFavorite).observe(getViewLifecycleOwner(), restaurants -> {
            Log.e("queryListView",query);
            for(Restaurant restaurant : restaurants){
                Log.e("ListViewSearchResult",restaurant.getName()+" : "+restaurant.getFormatted_address());
            }
            setUpRecyclerView(restaurants);
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setViewAndBinding(inflater,container);
        setViewModel();
        setListIsFavorite();
        getRestaurantListOrRestaurantListFavorites(listIsFavorite);
        return view;
    }

    public void setViewAndBinding(LayoutInflater inflater, ViewGroup container){
        fragmentListViewBinding = FragmentListViewBinding.inflate(inflater,container,false);
        view = fragmentListViewBinding.getRoot();
    }

    public void setViewModel(){
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    public void setListIsFavorite(){
        listIsFavorite = requireArguments().getBoolean("isListFavorite");
    }

    public void getRestaurantListOrRestaurantListFavorites(Boolean listIsFavorite){
        if(listIsFavorite){
            restaurantViewModel.getFavoritesListRestaurant().observe(getViewLifecycleOwner(), this::setUpRecyclerView);
        }else {
            restaurantViewModel.getListRestaurant().observe(getViewLifecycleOwner(), this::setUpRecyclerView);
        }
    }


    public void setUpRecyclerView(List<Restaurant> restaurantList){
        RecyclerView rvRestaurants = view.findViewById(R.id.recycler_view_list_view);
        rvRestaurants.setAdapter(new ListViewAdapter(restaurantList,this.getContext()));
        rvRestaurants.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}