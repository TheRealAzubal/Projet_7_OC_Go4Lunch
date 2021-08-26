package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.ui.Activities.MainActivity;

public class ListViewFragment extends Fragment {

    MainActivity mainActivity;

    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainActivity();
        mainActivity.setToolbarTitle("I'm Hungry!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }
}