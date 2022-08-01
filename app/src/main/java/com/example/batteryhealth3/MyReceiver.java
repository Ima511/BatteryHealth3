package com.example.batteryhealth3;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.BatteryManager.ACTION_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.UnusedAppRestrictionsConstants;
import androidx.core.content.res.ResourcesCompat;

import java.util.concurrent.atomic.AtomicInteger;

import pl.droidsonroids.gif.GifImageView;


public class MyReceiver extends BroadcastReceiver {

    // Declaration of Required view on Screen
    TextView
            tv_TimeLeftValue,
            tv_AvailabilityValue,
            tv_NominalVolValue,
            tv_ConditionValue,
            tv_tempValue,
            tv_TechnologyName,
            tv_Discharging,
            tv_ChargingSourceValue,
            tv_cycleCountValue;

    ImageView imageView;
    GifImageView gif;

    private String CHANNEL_ID = "Battery Channel";
//     Notification Codes

    private static final int NOTIFICATION_ID = 2;
    private static final int PI_REQ_CODE = 100;


    public MyReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        tv_TimeLeftValue = (TextView) ((Activity) context).findViewById(R.id.tv_TimeLeftValue);
        tv_NominalVolValue = (TextView) ((Activity) context).findViewById(R.id.tv_NominalVolValue);
        tv_ConditionValue = (TextView) ((Activity) context).findViewById(R.id.tv_ConditionValue);
        tv_TechnologyName = (TextView) ((Activity) context).findViewById(R.id.tv_TechnologyName);
        tv_tempValue = (TextView) ((Activity) context).findViewById(R.id.tv_tempValue);
        tv_Discharging = (TextView) ((Activity) context).findViewById(R.id.tv_Discharging);
        tv_ChargingSourceValue = (TextView) ((Activity) context).findViewById(R.id.tv_ChargingSourceValue);
        tv_cycleCountValue = (TextView) ((Activity) context).findViewById(R.id.tv_cycleCountValue);
        imageView = (ImageView) ((Activity) context).findViewById(R.id.imageView);
        gif = (GifImageView) ((Activity) context).findViewById(R.id.gif);


//        String actionString = intent.getAction();
//        Toast.makeText(context, actionString, Toast.LENGTH_SHORT).show();
//
//
//        // Are we charging / charged?
//        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;
//
//// How are we charging?
//        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
//        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
//        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
//
//        // Display whatever the state in the form of a Toast
//        if(isCharging) {
//            Toast.makeText(context, "Battery is charging ", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Toast.makeText(context, "Battery is not charging ", Toast.LENGTH_SHORT).show();
//        }

        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
//             Battery Level
            tv_TimeLeftValue.setText(String.valueOf(intent.getIntExtra("level", 0)) + "%");

//             Battery Voltage
            float voltTemp = (float) (intent.getIntExtra("voltage", 0) * 0.001);
            tv_NominalVolValue.setText(voltTemp + "V");

            // Battery Condition Function
            setCondition(intent);

            // Technology Name
            tv_TechnologyName.setText(intent.getStringExtra("technology"));

            // Battery Temperature
            float tempFloat = (float) intent.getIntExtra("temperature", -1) / 10;
            tv_tempValue.setText(tempFloat + "\u2103");

            // Cycle Count *Current* in Battery
            int currentValue = (int) intent.getIntExtra("current", -1);
            tv_cycleCountValue.setText(currentValue + "mA");


            // Charging Status Function
            setChargingStatus(context, intent);

            // Charging Source Function
            getChargingSource(intent);

            // Notification Fuction
            setNotification(context, intent);

        }





    }

    private void getChargingSource(Intent intent) {
        int sourceTemp = intent.getIntExtra("plugged", -1);

        switch (sourceTemp) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                tv_ChargingSourceValue.setText("AC");
                break;

            case BatteryManager.BATTERY_PLUGGED_USB:
                tv_ChargingSourceValue.setText("USB");
                break;

            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                tv_ChargingSourceValue.setText("WIRELESS");
                break;

            default:
                tv_ChargingSourceValue.setText("NULL");
                break;

        }
    }

    private void setChargingStatus(Context context, Intent intent) {
        int statusTemp = intent.getIntExtra("status", -1);


        switch ((statusTemp)) {
            case BATTERY_STATUS_UNKNOWN:
                tv_Discharging.setText("Unknown");
                imageView.setVisibility(View.GONE);
                gif.setVisibility(View.GONE);
                //  abc(context, "UNKNOWN", "STATUS UNKNOWN");
                //getNotification(context, "Charging Status unknown", "BatteryHealth");
                break;

            case BatteryManager.BATTERY_STATUS_CHARGING:
                tv_Discharging.setText("Charging");
                imageView.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);

                // abc(context, "Power", "Charging");
                // getNotification(context, "Battery is Charging ", "Power is Plugged in");
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                tv_Discharging.setText("Discharging");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(context, intent);
                //abc(context, "Power", "Discharging");
                // getNotification(context, "Battery is Discharging", "Battery Health");
                break;

            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                tv_Discharging.setText("Not Charging");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(context, intent);
                // abc(context, "Power", "Not Charging");
                // getNotification(context, "Battery is not charging", "Battery Health");
                break;


            case BatteryManager.BATTERY_STATUS_FULL:
                tv_Discharging.setText("Battery Full");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(context, intent);
                //getNotification(context, "Battery is Full", "Battery Health");
                break;

            default:
                tv_Discharging.setText("Null");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(context, intent);
                break;

        }
    }


//    private void getNotification(Intent intent) {
//        int statusTemp = intent.getIntExtra("status", -1);
//
//        switch ((statusTemp)) {
//            case BatteryManager.BATTERY_STATUS_UNKNOWN:
//
//                break;
//
//            case BatteryManager.BATTERY_STATUS_CHARGING:
//
//
//                break;
//
//            case BatteryManager.BATTERY_STATUS_DISCHARGING:
//
//                break;
//
//            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
//
//                break;
//
//
//            case BatteryManager.BATTERY_STATUS_FULL:
//
//                break;
//
//
//            default:
//                tv_Discharging.setText("Null");
//
//                break;
//
//        }
//    }

//    private void abc(Context context, String body, String title) {
//        Intent i = new Intent(context, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pi = PendingIntent.getActivity(context, PI_REQ_CODE, i, 0);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("1", "My Notification", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription(body);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "1");
//
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.app_icon)
//                .setTicker("Hear2")
//                .setPriority(Notification.PRIORITY_MAX)
//                .setOngoing(true)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setColor(context.getResources().getColor(R.color.colorRed))
//                .setContentInfo("Info")
//                .setContentIntent(pi);
//
//        notificationManager.notify(0,notificationBuilder.build());
//    }

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

    private void getBatteryImage(Context context, Intent intent) {

        int charge = (int) intent.getIntExtra("level", 0);


        if (charge == 0) {
            imageView.setImageResource(R.drawable.batteryzero);

        } else if (charge > 0 && charge <= 20) {
            imageView.setImageResource(R.drawable.batteryone);
            //    getNotification(context, "Battery is Low", "Plugged your device");


        } else if (charge > 20 && charge <= 40) {
            imageView.setImageResource(R.drawable.batterytwo);

        } else if (charge > 40 && charge <= 60) {
            imageView.setImageResource(R.drawable.batterythree);

        } else if (charge > 60 && charge <= 80) {
            imageView.setImageResource(R.drawable.batteryfour);
        } else if (charge > 80 && charge < 100) {

            imageView.setImageResource(R.drawable.batteryfive);
        } else if (charge == 100) {
            imageView.setImageResource(R.drawable.batteryfive);
            // getNotification(context, "Battery is Full", "Remove your charger");

        } else {

        }
    }


    private void setCondition(Intent intent) {
        int val = intent.getIntExtra("health", 0);

        switch (val) {
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                tv_ConditionValue.setText("UNKNOWN");
                break;

            case BatteryManager.BATTERY_HEALTH_GOOD:
                tv_ConditionValue.setText("GOOD");
                break;

            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                tv_ConditionValue.setText("OVERHEAT");
                break;

            case BatteryManager.BATTERY_HEALTH_DEAD:
                tv_ConditionValue.setText("DEAD");
                break;

            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                tv_ConditionValue.setText("OVER VOLTAGE");
                break;

            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                tv_ConditionValue.setText("FAILED");
                break;

            case BatteryManager.BATTERY_HEALTH_COLD:
                tv_ConditionValue.setText("COLD");
                break;
        }
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

// Notification for temperature factor of Battery

        float tempFloat = (float) intent.getIntExtra("temperature", -1) / 10;
        if (tempFloat > 50) {
            getNotification(context, "Take action", "Battery is overheated");
        }

// Notification for Different Condition of Battery Health

        int val = intent.getIntExtra("health", 0);

        switch (val) {

//            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
//                //  tv_ConditionValue.setText("UNKNOWN");
//                getNotification(context, "Take action", "Battery health is unknown");
//                break;

//            case BatteryManager.BATTERY_HEALTH_GOOD:
//                //    tv_ConditionValue.setText("GOOD");
//                getNotification(context, "Good", "Battery health is in good condition");
//                break;

            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                //  tv_ConditionValue.setText("OVERHEAT");
                getNotification(context, "Close the apps running in background", "Device is overheating");
                break;

            case BatteryManager.BATTERY_HEALTH_DEAD:
                //  tv_ConditionValue.setText("DEAD");
                getNotification(context, "Poor battery condition ", "Battery is dead");
                break;

            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                //   tv_ConditionValue.setText("OVER VOLTAGE");
                getNotification(context, "Use Proper Adapter to charge the Device", "Device is over voltage ");
                break;

            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                //   tv_ConditionValue.setText("FAILED");
                getNotification(context, "Battery Error", "Unspecified failure of battery");
                break;

            case BatteryManager.BATTERY_HEALTH_COLD:
                //   tv_ConditionValue.setText("COLD");
                getNotification(context, "Battery is cooling ", "Battery health is cold");
                break;


        }

    }
}