package com.azubal.go4lunch.workerManager;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.ui.Activities.DetailActivity;
import com.azubal.go4lunch.viewmodels.RestaurantViewModel;
import com.azubal.go4lunch.viewmodels.UserViewModel;

public class ReminderRestaurantWorker extends Worker {

    String NOTIFICATION_ID = "appName_notification_id";
    String NOTIFICATION_CHANNEL = "appName_channel_01";
    RestaurantViewModel restaurantViewModel;
    Restaurant restaurantChosen;

    public ReminderRestaurantWorker(@NonNull Context context, @NonNull WorkerParameters params, Restaurant restaurant) {
        super(context, params);
        restaurantChosen = restaurant;
        restaurantViewModel = new ViewModelProvider((ViewModelStoreOwner) getApplicationContext()).get(RestaurantViewModel.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        sendNotification();
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    public void sendNotification(){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("restaurantId",restaurantChosen.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.logo_go4lunch)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentText(getApplicationContext().getString(R.string.app_name)+" "+restaurantChosen.getName()+" ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.app_name);
            String description = getApplicationContext().getString(R.string.app_name)+" "+restaurantChosen.getName()+" ";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void deleteRestaurantChosenAt12PM(){
        restaurantViewModel.setRestaurantChosen(null);
    }

}
