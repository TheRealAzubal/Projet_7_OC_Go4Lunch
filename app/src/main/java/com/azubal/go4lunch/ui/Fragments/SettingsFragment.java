package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentSettingsBinding;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    View view;
    public SettingsFragment() {}
    UserViewModel userViewModel;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setBinding(inflater, container);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            binding.editUsername.setText(user.getUsername());
            binding.editEmail.setText(user.getEmail());

            Glide.with(this)
                    .load(user.getUrlPicture())
                    .circleCrop()
                    .placeholder(R.drawable.image_not_found)
                    .into(binding.profilePicture);
        });

        binding.editUsernameButton.setOnClickListener(view -> userViewModel.setUserName(Objects.requireNonNull(binding.editUsername.getText()).toString()));
        binding.editEmailButton.setOnClickListener(view -> userViewModel.setUserEmail(Objects.requireNonNull(binding.editEmail.getText()).toString()));

        return view;
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
    }
}