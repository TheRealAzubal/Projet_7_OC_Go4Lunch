package com.azubal.go4lunch.ui.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentSettingsBinding;
import com.azubal.go4lunch.ui.Activities.MainActivity;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    View view;
    MainActivity mainActivity;

    public SettingsFragment() {
    }

    String uid;
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

        getMainActivity();

        userViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            binding.editUsername.setText(user.getUsername());
            binding.editEmail.setText(user.getEmail());

            uid = user.getUid();

            Glide.with(this)
                    .load(user.getUrlPicture())
                    .circleCrop()
                    .placeholder(R.drawable.image_not_found)
                    .into(binding.profilePicture);
        });

        binding.editUsernameButton.setOnClickListener(view -> userViewModel.setUserName(Objects.requireNonNull(binding.editUsername.getText()).toString()));
        binding.editEmailButton.setOnClickListener(view -> userViewModel.setUserEmail(Objects.requireNonNull(binding.editEmail.getText()).toString()));

        binding.imageButtonEdit.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(
                    this.requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                getLocalPicture();

            } else {
                requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }


        });

        return view;
    }

    public void getMainActivity() {
        mainActivity = (MainActivity) getActivity();
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getLocalPicture();
                }
            });

    public void getLocalPicture() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(i);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) { //SUCCESS
                    Intent data = result.getData();
                    Uri uriImageSelected = Objects.requireNonNull(data).getData();
                    Log.e("uri", uriImageSelected.toString());

                    final StorageReference ref1 = FirebaseStorage.getInstance().getReference().child("profile_pictures/" + "/" + uid);

                    ref1.delete().addOnSuccessListener(unused -> {});


                    StorageReference mImageRef = FirebaseStorage.getInstance().getReference("profile_pictures"+"/").child(uid);
                    UploadTask uploadTask = mImageRef.putFile(uriImageSelected);






                    uploadTask.addOnFailureListener(exception -> {}).addOnSuccessListener(taskSnapshot -> {});

                    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("profile_pictures/" + "/" + uid);

                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            userViewModel.setPhotoUrl(downloadUri.toString());

                            mainActivity.displayNewProfilePicture(downloadUri.toString());

                            Glide.with(this)
                                    .load(downloadUri)
                                    .circleCrop()
                                    .placeholder(R.drawable.image_not_found)
                                    .into(binding.profilePicture);
                        }
                    });


                }
            });













}