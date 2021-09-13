package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.azubal.go4lunch.databinding.FragmentLogOutBinding;
import com.azubal.go4lunch.manager.UserManager;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;
    UserManager userManager;

    public LogOutFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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
        logOutButtonListener();
        deleteAccountButtonListener();
        return binding.getRoot();
    }

    private void logOutButtonListener(){
        binding.LogOutButton.setOnClickListener(view -> {
            userManager.signOut(getContext()).addOnSuccessListener(aVoid -> {
                requireActivity().finish();

            });
        });
    }

    private void deleteAccountButtonListener(){
        binding.deleteAccountButton.setOnClickListener(view -> {
            new AlertDialog.Builder(requireContext())
                    .setMessage("Voulez vous vraiment supprimer votre compte")
                    .setPositiveButton("Confirmer", (dialogInterface, i) ->
                            userManager.deleteUser(getContext())
                                    .addOnSuccessListener(aVoid -> {
                                                requireActivity().finish();
                                            }
                                    )
                    )
                    .setNegativeButton("Annuler", null)
                    .show();

        });
    }

}