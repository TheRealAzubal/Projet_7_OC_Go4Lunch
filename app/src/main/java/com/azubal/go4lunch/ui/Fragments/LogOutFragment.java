package com.azubal.go4lunch.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentLogOutBinding;
import com.azubal.go4lunch.databinding.FragmentMapViewBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;

public class LogOutFragment extends Fragment {

    MainActivity mainActivity;
    private FragmentLogOutBinding binding;
    View view;

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainActivity();
        mainActivity.setToolbarTitle(getString(R.string.logout));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    public void getMainActivity() { mainActivity = (MainActivity) getActivity(); }

}