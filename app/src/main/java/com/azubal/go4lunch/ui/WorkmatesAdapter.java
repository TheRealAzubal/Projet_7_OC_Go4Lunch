package com.azubal.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.models.User;
import com.azubal.go4lunch.ui.Activities.DetailActivity;
import com.bumptech.glide.Glide;
import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolder> {

    private final List<User> userList ;
    Context context;

    public WorkmatesAdapter(List<User> items, Context contextActivity){
        userList = items;
        context = contextActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_workmates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        User user = userList.get(position);

        if(user.getRestaurantChosenAt12PM() == null){
            holder.workmateName.setText(user.getUsername()+" "+context.getString(R.string.workmatesHasntDecidedYet));
        }else {
            holder.workmateName.setText(user.getUsername()+" ("+user.getRestaurantChosenAt12PM().getName()+")");
        }

        holder.itemView.setOnClickListener(view -> {
            if(user.getRestaurantChosenAt12PM() != null){
                launchDetailRestaurant(user.getRestaurantChosenAt12PM(), holder.itemView.getContext());
            }
        });




        Glide.with(context)
                .load(user.getUrlPicture())
                .placeholder(R.drawable.outline_block_24)
                .circleCrop()
                .into(holder.workmateAvatar);

    }

    public void launchDetailRestaurant(Restaurant restaurant, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("restaurant_id", restaurant.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView workmateAvatar;
        public TextView workmateName;


        public ViewHolder(View view) {
            super(view);
            workmateAvatar = view.findViewById(R.id.item_workmate_avatar);
            workmateName = view.findViewById(R.id.item_workmatesname);
        }
    }
}
