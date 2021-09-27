package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.azubal.go4lunch.databinding.FragmentLogOutBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;
import com.azubal.go4lunch.viewmodels.AuthAppViewModel;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;
    MainActivity mainActivity;
    AuthAppViewModel authAppViewModel;


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
        setUpViewModel();
        logOutButtonListener();
        deleteAccountButtonListener();
        return binding.getRoot();
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }

    private void setUpViewModel(){
        authAppViewModel = new ViewModelProvider(this).get(AuthAppViewModel.class);
    }

    private void logOutButtonListener(){
        binding.LogOutButton.setOnClickListener(view -> {
            authAppViewModel.signOut();
            mainActivity.startActivityLogin();
            mainActivity.finishMainActivity();
        });
    }

    private void deleteAccountButtonListener(){
        binding.deleteAccountButton.setOnClickListener(view -> {
            authAppViewModel.deleteUser();
            mainActivity.startActivityLogin();
            mainActivity.finishMainActivity();
        });
    }



}