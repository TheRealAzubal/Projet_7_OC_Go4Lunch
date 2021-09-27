package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.azubal.go4lunch.databinding.FragmentMapViewBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;

public class MapViewFragment extends Fragment {

    MainActivity mainActivity;
    private FragmentMapViewBinding binding;
    View view;

    public MapViewFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }
}