package com.azubal.go4lunch.ui;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azubal.go4lunch.R;

import com.azubal.go4lunch.models.Restaurant;

import java.util.List;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurants;


    public ListViewAdapter(List<Restaurant> items) {
      restaurants = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_view_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        Log.e("onBindViewHolder",position+" , "+ restaurant.getName());
        holder.txViewName.setText(restaurant.getName());
        holder.txViewAddress.setText(restaurant.getAddress());
        if(restaurant.getOpen()){
            holder.txViewOpening.setText("true");
        }else{
            holder.txViewOpening.setText("false");
        }
        holder.txViewDistance.setText(restaurant.getDistance()+"");
        holder.imgWorkmatesIcon.setVisibility(View.VISIBLE);

        if(restaurant.getRating() == 3){
            holder.imgStar1.setVisibility(View.VISIBLE);
            holder.imgStar2.setVisibility(View.VISIBLE);
            holder.imgStar3.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(v -> {
        });
    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txViewName;
        public TextView txViewAddress;
        public TextView txViewOpening;
        public TextView txViewDistance;
        public ImageView imgWorkmatesIcon;
        public TextView txViewNumberWorkmates;
        public ImageView imgStar1;
        public ImageView imgStar2;
        public ImageView imgStar3;
        public ImageView imgRestaurant;


        public ViewHolder(View view) {
            super(view);
            txViewName = view.findViewById(R.id.res_list_name);
            txViewAddress = view.findViewById(R.id.res_list_address);
            txViewOpening = view.findViewById(R.id.res_list_opening);
            txViewDistance = view.findViewById(R.id.res_list_distance);
            imgWorkmatesIcon = view.findViewById(R.id.res_list_workmates_ic);
            txViewNumberWorkmates = view.findViewById(R.id.res_list_workmates_tv);
            imgStar1 = view.findViewById(R.id.res_list_star_1);
            imgStar2 = view.findViewById(R.id.res_list_star_2);
            imgStar3 = view.findViewById(R.id.res_list_star_3);
            imgRestaurant = view.findViewById(R.id.res_list_iv);
        }
    }
}