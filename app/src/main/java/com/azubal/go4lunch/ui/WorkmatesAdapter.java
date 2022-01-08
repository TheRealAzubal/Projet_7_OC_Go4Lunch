package com.azubal.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.User;
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


        holder.workmateName.setText(user.getUsername());

        Glide.with(context)
                .load(user.getUrlPicture())
                .placeholder(R.drawable.shape_user_image)
                .circleCrop()
                .into(holder.workmateAvatar);

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
