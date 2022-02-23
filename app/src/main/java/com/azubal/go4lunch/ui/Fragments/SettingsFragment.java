package com.azubal.go4lunch.ui.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.azubal.go4lunch.databinding.FragmentSettingsBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;

public class SettingsFragment extends Fragment {

    MainActivity mainActivity;
    private FragmentSettingsBinding binding;
    View view;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    String NOTIFICATION_CHANNEL = "appName_channel_01";

    public SettingsFragment() {}

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


        mainActivity = (MainActivity) getActivity();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = requireActivity().getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL);

               binding.switchNotification.setChecked(false);

               if(notificationChannel.getImportance() == NotificationManager.IMPORTANCE_HIGH ){
                   binding.switchNotification.setChecked(true);
               }

                //binding.switchNotification.setOnCheckedChangeListener((compoundButton, b) -> {
                    //compoundButton.setChecked(b);

                    //if(!compoundButton.isChecked()){
                        //notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);

                    //}else{
                        //notificationChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
                    //}

                //});

            }else {
                binding.switchNotification.setVisibility(View.INVISIBLE);
            }

        }





        return view;
    }

    public void disableNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
        }
    }

    public void enableNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        }
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
    }
}