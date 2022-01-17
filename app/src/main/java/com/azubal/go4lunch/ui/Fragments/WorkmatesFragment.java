package com.azubal.go4lunch.ui.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentWorkmatesBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.ui.ListViewAdapter;
import com.azubal.go4lunch.ui.WorkmatesAdapter;
import com.azubal.go4lunch.viewmodels.AuthAppViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    View view;
    AuthAppViewModel authAppViewModel;

    public WorkmatesFragment() {
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
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        authAppViewModel = new ViewModelProvider(requireActivity()).get(AuthAppViewModel.class);

        authAppViewModel.getAllUsers().observe(this, this::setUpRecyclerView);




        return view;
    }

    public void setUpRecyclerView(List<User> userList){
        RecyclerView rvUsers = view.findViewById(R.id.list_workmates);
        rvUsers.setAdapter(new WorkmatesAdapter(userList , getContext()));
        rvUsers.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}