package com.azubal.go4lunch.workerManager;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.azubal.go4lunch.R;
import com.azubal.go4lunch.ui.Activities.DetailActivity;

import java.util.Arrays;


public class ReminderRestaurantWorker extends Worker {


    String NOTIFICATION_CHANNEL = "appName_channel_01";
    String restaurantId;
    String restaurantName;
    String restaurantAddress;
    String [] restaurantListWorkmates;
    StringBuilder sbf;


    public ReminderRestaurantWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        restaurantId = getInputData().getString("restaurantId");
        restaurantName= getInputData().getString("restaurantName");
        restaurantAddress = getInputData().getString("restaurantAddress");
        restaurantListWorkmates = getInputData().getStringArray("restaurantListWorkmates");
        sendNotification();
        return Result.success();
    }

    public void sendNotification(){

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < restaurantListWorkmates.length; i++) {
            stringBuilder.append(restaurantListWorkmates[i]+" "+getApplicationContext().getString(R.string.wormatesJoining));
        }
        String joinedString = stringBuilder.toString();


                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("restaurantId",restaurantId);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.logo_go4lunch)
                        .setContentTitle(getApplicationContext().getString(R.string.reminderRestaurantTitleNotification))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getApplicationContext().getString(R.string.reminderRestaurantDescriptionNotification)+restaurantName+","+"\n"+restaurantAddress+","+"\n"+joinedString))
                        .setAutoCancel(true);



                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(0, builder.build());
            }

}
