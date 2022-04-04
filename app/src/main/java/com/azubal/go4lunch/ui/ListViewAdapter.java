package com.azubal.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.ApiDetails.Period;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.Activities.DetailActivity;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurants;
    Context context;
    int minute, monthOfYear, dayMonth, dayOfWeek, hour, year;
    DateTime actualDateTime;
    UserViewModel userViewModel;

    public ListViewAdapter(List<Restaurant> items, Context context) {
        restaurants = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_view_item, parent, false);
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) view.getContext()).get(UserViewModel.class);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.txViewName.setText(restaurant.getName());
        holder.txViewAddress.setText(restaurant.getAddress());
        holder.txViewDistance.setText(restaurant.getDistance());
        holder.imgWorkmatesIcon.setVisibility(View.VISIBLE);
        userViewModel.getAllUsersPickForThisRestaurant(restaurant,false).observe((LifecycleOwner) holder.itemView.getContext(), users -> holder.txViewNumberWorkmates.setText("("+users.size()+")"));
        holder.txViewNumberWorkmates.setVisibility(View.VISIBLE);
        holder.txViewAddress.setText(restaurant.getAddress());
        holder.txViewAddress.setText(restaurant.getFormatted_address());

        if (Math.round(restaurant.getRating()) == 1) {
            holder.imgStar5.setVisibility(View.VISIBLE);
        } else if (Math.round(restaurant.getRating()) == 2) {
            holder.imgStar5.setVisibility(View.VISIBLE);
            holder.imgStar4.setVisibility(View.VISIBLE);
        } else if (Math.round(restaurant.getRating()) == 3) {
            holder.imgStar5.setVisibility(View.VISIBLE);
            holder.imgStar4.setVisibility(View.VISIBLE);
            holder.imgStar3.setVisibility(View.VISIBLE);
        }

        Glide.with(context)
                .load(restaurant.getPhotoUrl())
                .placeholder(R.drawable.image_not_found)
                .into(holder.imgRestaurant);

        holder.itemView.setOnClickListener(v -> {
            final Context contextRaw = holder.itemView.getContext();
            launchDetailRestaurant(restaurant, contextRaw,position);
        });

        final Calendar c = Calendar.getInstance();
        minute = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR_OF_DAY);
        year = c.get(Calendar.YEAR);
        dayMonth = c.get(Calendar.DAY_OF_MONTH);
        monthOfYear = c.get(Calendar.MONTH) + 1;
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        actualDateTime = new DateTime(year, monthOfYear, dayMonth, hour, minute);

        boolean isClosed = true;

        if (restaurant.getOpeningHours() == null) {
            holder.txViewOpening.setText(R.string.hoursIsUnknow);
        }else {
            for (Period period : restaurant.getOpeningHours().getPeriods()) {
                if(period.getOpen() != null && period.getClose() != null) {
                    if (period.getOpen().getDay() == dayOfWeek && period.getClose().getDay() >= dayOfWeek) {
                        int openTime = Integer.parseInt(period.getOpen().getTime());
                        int closeTime = Integer.parseInt(period.getClose().getTime());
                        DateTime dateTimeOpen = new DateTime(year, monthOfYear, dayMonth, openTime / 100, openTime % 100);
                        DateTime dateTimeClose = new DateTime(year, monthOfYear, dayMonth + (period.getClose().getDay() - dayOfWeek), closeTime / 100, closeTime % 100);

                        if (actualDateTime.isAfter(dateTimeOpen) && actualDateTime.isBefore(dateTimeClose)) {
                            holder.txViewOpening.setTextColor(Color.BLACK);
                            holder.txViewOpening.setText(context.getString(R.string.hoursOpenUntil) +" "+ new LocalTime(dateTimeClose.getHourOfDay(), dateTimeClose.getMinuteOfHour()).toString("hh:mm a"));
                            if (actualDateTime.isAfter(dateTimeClose.plusMinutes(-30))) {
                                holder.txViewOpening.setTextColor(Color.RED);
                                holder.txViewOpening.setText(context.getString(R.string.ClosingSoon));
                            }
                            isClosed = false;
                            break;
                        }
                        if (actualDateTime.isBefore(dateTimeOpen) && actualDateTime.isAfter(dateTimeOpen.plusMinutes(-30))) {
                            holder.txViewOpening.setTextColor(Color.GREEN);
                            holder.txViewOpening.setText(context.getString(R.string.OpeningSoon));
                            isClosed = false;
                        }
                    }
                }
            }
        }

        if (isClosed ){
            holder.txViewOpening.setTextColor(Color.BLACK);
            holder.txViewOpening.setText(context.getString(R.string.Closed));
        }
            if (restaurant.getOpeningHours() != null) {
                if (restaurant.getOpeningHours().getWeekdayText().get(0).equals("Monday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(1).equals("Tuesday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(2).equals("Wednesday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(3).equals("Thursday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(4).equals("Friday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(5).equals("Saturday: Open 24 hours") &&
                        restaurant.getOpeningHours().getWeekdayText().get(6).equals("Sunday: Open 24 hours")
                ) {
                    holder.txViewOpening.setText(context.getString(R.string.Open24_7));
                }
            }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void launchDetailRestaurant(Restaurant restaurant, Context context,int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("restaurant_id", restaurant.getId());
        intent.putExtra("restaurantPosition",position);
        context.startActivity(intent);
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
        public ImageView imgStar4;
        public ImageView imgStar5;
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
            imgStar4 = view.findViewById(R.id.res_list_star_4);
            imgStar5 = view.findViewById(R.id.res_list_star_5);
            imgRestaurant = view.findViewById(R.id.res_list_iv);
        }
    }
}