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
import com.azubal.go4lunch.viewmodels.UserViewModel;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;
    MainActivity mainActivity;
    UserViewModel authAppViewModel;


    public LogOutFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        setBinding(inflater, container);
        getMainActivity();
        setUpViewModel();
        logOutButtonListener();
        deleteAccountButtonListener();
        return binding.getRoot();
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container){
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }

    private void setUpViewModel(){
        authAppViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void logOutButtonListener(){
        binding.LogOutButton.setOnClickListener(view -> {
            authAppViewModel.signOut(LogOutFragment.this.getContext());
            mainActivity.startActivityLogin();
            mainActivity.finishMainActivity();
        });
    }

    private void deleteAccountButtonListener(){
        binding.deleteAccountButton.setOnClickListener(view -> {
            authAppViewModel.deleteUser(LogOutFragment.this.getContext());
            mainActivity.startActivityLogin();
            mainActivity.finishMainActivity();
        });
    }



}