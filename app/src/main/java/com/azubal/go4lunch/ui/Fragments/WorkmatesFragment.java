package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentWorkmatesBinding;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.ui.WorkmatesAdapter;
import com.azubal.go4lunch.viewmodels.UserViewModel;

import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    View view;
    UserViewModel userViewModel;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setBinding(inflater, container);
        setViewModel();
        getAllUsers();
        return view;
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container){
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        view = binding.getRoot();
    }

    public void setViewModel(){
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    public void getAllUsers(){
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), this::setUpRecyclerView);
    }

    public void setUpRecyclerView(List<User> userList){
        RecyclerView rvUsers = view.findViewById(R.id.list_workmates);
        rvUsers.setAdapter(new WorkmatesAdapter(userList , getContext()));
        rvUsers.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}