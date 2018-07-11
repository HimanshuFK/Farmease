package com.farmease.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.farmease.app.HomeActivity;
import com.farmease.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Himanshu on 22/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "RECEIVED_NOTI";
    private int notifId = 1337;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.

        if (remoteMessage.getData().get("image").trim().equalsIgnoreCase("")) {
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));

        } else {
           // showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("image"));
           // Log.e(TAG, "with: ");
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void showNotification(String title, String message, final String image) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Default stuff; making and showing notification
        final Context context = getApplicationContext();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.appicon) // Needed for the notification to work/show!!
                .setContentTitle(title)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigPictureStyle())
                .build();

        notificationManager.notify(notifId, notification);
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            // Get RemoteView and id's needed
            final RemoteViews contentView = notification.contentView;
            final int iconId = android.R.id.icon;

            // Uncomment for BigPictureStyle, Requires API 16!
            final RemoteViews bigContentView = notification.bigContentView;
            final int bigIconId = getResources().getIdentifier("android:id/big_picture", null, null);

            // Use Picasso with RemoteViews to load image into a notification
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                   /* Picasso.with(context)
                            .load(WebServices.BaseImage + image)
                            .resize(600, 400)
                            .into(bigContentView, bigIconId, notifId++, notification);*/
                }
            });
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.appicon) // Needed for the notification to work/show!!
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setColor(this.getResources().getColor(R.color.colorPrimary))
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(777, notificationBuilder.build());
    }

}
