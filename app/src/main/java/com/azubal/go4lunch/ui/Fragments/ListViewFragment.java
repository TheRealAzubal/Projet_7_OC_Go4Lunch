package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.ListViewAdapter;


import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    List<Restaurant> listRestaurants = new ArrayList<>() ;

    public ListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        Restaurant restaurant1 = new Restaurant("Test", "Test","Test",3,true,300);
        listRestaurants.add(restaurant1);
        listRestaurants.add(restaurant1);


        RecyclerView rvRestaurants = view.findViewById(R.id.recycler_view_list_view);
        rvRestaurants.setAdapter(new ListViewAdapter(listRestaurants));
        rvRestaurants.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}