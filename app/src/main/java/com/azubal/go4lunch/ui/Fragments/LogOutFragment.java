package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.azubal.go4lunch.databinding.FragmentLogOutBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;
    MainActivity mainActivity;


    public LogOutFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);


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
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        getMainActivity();
        logOutButtonListener();
        deleteAccountButtonListener();
        return binding.getRoot();
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }

    private void logOutButtonListener(){
        binding.LogOutButton.setOnClickListener(view -> {
            mainActivity.logOutUser();
        });
    }

    private void deleteAccountButtonListener(){
        binding.deleteAccountButton.setOnClickListener(view -> {
            mainActivity.deleteUserAccount();

        });
    }

}