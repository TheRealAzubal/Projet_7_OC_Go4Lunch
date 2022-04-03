package com.azubal.go4lunch.ui.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.databinding.FragmentDetailBinding;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.ui.WorkmatesPickRestaurantAdapter;
import com.azubal.go4lunch.utils.ToastUtil;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class DetailFragment extends Fragment {

    Restaurant restaurantLocal;
    String restaurantId;
    RestaurantViewModel restaurantViewModel;
    UserViewModel userViewModel;
    FragmentDetailBinding fragmentDetailBinding;

    public DetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setBinding(inflater, container);
        setRestaurantId();
        setViewModel();
        getRestaurant();

        return fragmentDetailBinding.getRoot();
    }

    public void setBinding(LayoutInflater inflater, ViewGroup container){
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater,container,false);
    }

    public void setRestaurantId(){
        restaurantId = requireArguments().getString("restaurant_id");
    }

    public void setViewModel(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    public void getRestaurant(){
        userViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {


            if(!restaurantId.equals("")){
                restaurantViewModel.getRestaurantById(restaurantId).observe(getViewLifecycleOwner(), restaurant -> {
                    if (restaurant != null) {
                        restaurantLocal = restaurant;
                        setUpRestaurantUI();
                    }
                });
            }else {

                if(user.getRestaurantChosenAt12PM() == null){
                    ToastUtil.displayToastLong(getString(R.string.notChosenRestaurantToast),requireActivity());

                }else {

                    restaurantViewModel.getRestaurantById(user.getRestaurantChosenAt12PM().getId()).observe(getViewLifecycleOwner(), restaurant -> {
                        if (restaurant != null) {
                            restaurantLocal = restaurant;
                            setUpRestaurantUI();
                        }
                    });

                }

            }

        });

    }

    public void setUpRestaurantUI(){
        if(restaurantLocal.getFormatted_phone_number() == null){
            fragmentDetailBinding.phoneButton.setVisibility(View.INVISIBLE);
        }

        if(restaurantLocal.getWebsite() == null){
            fragmentDetailBinding.websiteButton.setVisibility(View.INVISIBLE);
        }



        fragmentDetailBinding.name.setText(restaurantLocal.getName());
        fragmentDetailBinding.detailFormattedAddress.setText(restaurantLocal.getFormatted_address());

        fragmentDetailBinding.phoneButton.setOnClickListener(view -> requestPermissionPhone());



        restaurantViewModel.isLikeRestaurant(restaurantLocal).observe(getViewLifecycleOwner(), aBoolean -> {
            fragmentDetailBinding.likeButton.setSelected(aBoolean);
            Log.e("isLike",aBoolean.toString());
        });

        restaurantViewModel.isPickRestaurant(restaurantLocal).observe(getViewLifecycleOwner(), aBoolean -> {
            fragmentDetailBinding.pickRestaurantButton.setSelected(aBoolean);
            Log.e("isPickRestaurant",aBoolean.toString());
        });

        fragmentDetailBinding.likeButton.setOnClickListener(view -> {
            fragmentDetailBinding.likeButton.setSelected(!fragmentDetailBinding.likeButton.isSelected());

            Log.e("likeButtonOnClick","aa");

            if (view.isSelected()) {
                restaurantViewModel.addRestaurantLike(restaurantLocal);
                //Handle selected state change
            } else {
                restaurantViewModel.deleteRestaurantLike(restaurantLocal);
            }

        });


        fragmentDetailBinding.pickRestaurantButton.setOnClickListener(view -> {

            fragmentDetailBinding.pickRestaurantButton.setSelected(!fragmentDetailBinding.pickRestaurantButton.isSelected());

            if (view.isSelected()) {

                userViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {

                    Restaurant restaurantChosen = user.getRestaurantChosenAt12PM();

                    if(restaurantChosen == null){
                        restaurantViewModel.addUserPickForRestaurant(restaurantLocal);
                    }else{
                        if(!restaurantChosen.getId().equals(restaurantLocal.getId())){
                            restaurantViewModel.deleteUserPickForRestaurant(restaurantChosen);
                            restaurantViewModel.addUserPickForRestaurant(restaurantLocal);
                        }else{
                            restaurantViewModel.addUserPickForRestaurant(restaurantLocal);
                        }
                    }

                });

            } else {
                restaurantViewModel.deleteUserPickForRestaurant(restaurantLocal);
            }

        });

        fragmentDetailBinding.websiteButton.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantLocal.getWebsite()))));

        Log.e("photoUrl",restaurantLocal.getPhotoUrl());

        Glide.with(this)
                .load(restaurantLocal.getPhotoUrl())
                .placeholder(R.drawable.logo_go4lunch)
                .centerCrop()
                .into(fragmentDetailBinding.imageRestaurant);

        if (Math.round(restaurantLocal.getRating()) ==0){

        }else if (Math.round(restaurantLocal.getRating()) == 1){
            fragmentDetailBinding.firstStar.setVisibility(View.VISIBLE);
        }else if (Math.round(restaurantLocal.getRating()) == 2){
            fragmentDetailBinding.firstStar.setVisibility(View.VISIBLE);
            fragmentDetailBinding.secondStar.setVisibility(View.VISIBLE);
        }else if (Math.round(restaurantLocal.getRating()) == 3){
            fragmentDetailBinding.firstStar.setVisibility(View.VISIBLE);
            fragmentDetailBinding.secondStar.setVisibility(View.VISIBLE);
            fragmentDetailBinding.thirdStar.setVisibility(View.VISIBLE);
        }

        userViewModel.getAllUsersPickForThisRestaurant(restaurantLocal).observe(getViewLifecycleOwner(), this::setUpRecyclerView);
    }

    private void requestPermissionPhone(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED ) {

            onRequestPermissionGranted();

        }else {
            requestPermissionLauncher.launch(
                    Manifest.permission.CALL_PHONE
            );
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

                if (isGranted) {
                    onRequestPermissionGranted();
                }

            });

    public void  onRequestPermissionGranted(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(getString(R.string.beforeNumberPhone)+ restaurantLocal.getFormatted_phone_number()));
        startActivity(callIntent);
    }

    public void setUpRecyclerView(List<User> userList){
        RecyclerView rvUsers = fragmentDetailBinding.recyclerViewDetailResto;
        rvUsers.setAdapter(new WorkmatesPickRestaurantAdapter(userList,requireActivity()));
        rvUsers.setLayoutManager(new LinearLayoutManager(fragmentDetailBinding.recyclerViewDetailResto.getContext()));
    }
}