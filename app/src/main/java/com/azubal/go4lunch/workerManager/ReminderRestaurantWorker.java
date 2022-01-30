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
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.ui.Activities.DetailActivity;


public class ReminderRestaurantWorker extends Worker {


    String NOTIFICATION_CHANNEL = "appName_channel_01";
    String restaurantId;
    String restaurantName;

    public ReminderRestaurantWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        restaurantId = getInputData().getString("restaurantId");
        restaurantName= getInputData().getString("restaurantName");

        sendNotification();
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    public void sendNotification(){
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("restaurantId",restaurantId);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.logo_go4lunch)
                        .setContentTitle(getApplicationContext().getString(R.string.reminderRestaurantTitleNotification))
                        .setContentText(getApplicationContext().getString(R.string.reminderRestaurantDescriptionNotification)+restaurantName)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getApplicationContext().getString(R.string.app_name);
                    String description = getApplicationContext().getString(R.string.app_name);
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(0, builder.build());
            }

}
