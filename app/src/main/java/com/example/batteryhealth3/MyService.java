package com.example.batteryhealth3;

import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;

import androidx.core.content.res.ResourcesCompat;

public class MyService extends Service {

    private BroadcastReceiver br;
    private String CHANNEL_ID = "Battery Channel";
//     Notification Codes

    private static final int NOTIFICATION_ID = 2;
    private static final int PI_REQ_CODE = 100;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }

            public void getNotification(Context context, String body, String title) {

                // Conversion of icon from drawable to Bitmap
                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.app_icon, null);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap largeIcon = bitmapDrawable.getBitmap();


                // Declaration of Notification Manager and initializing object
                NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                Notification notification_type1;

                Intent inotify = new Intent(context, MainActivity.class);
                inotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pi = PendingIntent.getBroadcast(context,
                        PI_REQ_CODE,
                        inotify,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                // Custom Notification creation
//        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.notification_large);


                // Big Picture Style
                Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                        .bigPicture(((BitmapDrawable) (ResourcesCompat.getDrawable(context.getResources(), R.drawable.app_icon, null))).getBitmap())
                        .bigLargeIcon(largeIcon)
                        .setBigContentTitle(title)
                        .setSummaryText(body);

//        // Inbox Style
//
//        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
//                .addLine(title)
//                .addLine(body);


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    notification_type1 = new Notification.Builder(context)
//                    .setStyle(new Notification.DecoratedCustomViewStyle())
//                    .setCustomContentView(notificationLayout)
//                    .setCustomBigContentView(notificationLayoutExpanded)
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentText(title)
                            .setSubText(body)
                            .setContentIntent(pi)
                            .setVibrate(new long[]{0, 1000, 500, 1000})
                            // .setStyle(inboxStyle)
                            .setStyle(bigPictureStyle)
                            .setChannelId(CHANNEL_ID)
                            .build();
                    nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));

                } else {

                    notification_type1 = new Notification.Builder(context)
//                    .setStyle(new Notification.DecoratedCustomViewStyle())
//                    .setCustomContentView(notificationLayout)
//                    .setCustomBigContentView(notificationLayoutExpanded)
                            .setLargeIcon(largeIcon)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentText(title)
                            .setSubText(body)
                            .setVibrate(new long[]{0, 1000, 500, 1000})
                            //.setStyle(inboxStyle)
                            .setStyle(bigPictureStyle)
                            .setContentIntent(pi)
                            .build();
                }
                nm.notify(NOTIFICATION_ID, notification_type1);


            }

            private void setNotification(Context context, Intent intent) {

                // Conditional Notification based on the Battery Status
                int charge = (int) intent.getIntExtra("level", 0);

                int statusTemp = intent.getIntExtra("status", -1);
                switch ((statusTemp)) {
                    case BATTERY_STATUS_UNKNOWN:
                        getNotification(context, "Take Action", "Battery Status Unknown");

                        break;

                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        getNotification(context, "Charger is Plugged in ", "Battery is Charging");
                        if (charge <= 20) {
                            getNotification(context, "Charger is Plugged in ", "Battery is Charging");
                        } else if (charge == 100) {
                            getNotification(context, "Remove the charger ", "Battery Full");
                        }
                        break;

                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        getNotification(context, "Charger is not Plugged", "Battery is Discharging");
                        if (charge <= 20) {
                            getNotification(context, "plug in the charger", "Battery is low");
                        }
                        break;

                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        getNotification(context, "Charger is not Plugged", "Battery is Nor Charging");
                        if (charge <= 20) {
                            getNotification(context, "plug in the charger", "Battery is low");
                        } else if (charge == 100) {
                            getNotification(context, "Do not Plugged in  charger ", "Battery Full");
                        }
                        break;


                    case BatteryManager.BATTERY_STATUS_FULL:
                        getNotification(context, "Do not Plugged in charger", "Battery is Full");

                        break;


                }
            }
        }
    }
}


