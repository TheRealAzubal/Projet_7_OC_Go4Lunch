package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.azubal.go4lunch.databinding.FragmentYourLunchBinding;

public class YourLunchFragment extends Fragment {

    private FragmentYourLunchBinding binding;
    View view;

    public YourLunchFragment() {
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
        binding = FragmentYourLunchBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }
}