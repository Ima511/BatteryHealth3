package com.example.batteryhealth3;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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

    //    private static final String CHANNEL_ID = "Battery Channel";
//    private static final int NOTIFICATION_ID = 2;
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
            setChargingStatus(context,intent);

            // Charging Source Function
            getChargingSource(intent);

//            // Time Left calculation
//
//            int timeLeft = (int) intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)/60000 ;
//            tv_TimeLeftValue.setText(timeLeft + " minute");

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

    private void setChargingStatus(Context context ,Intent intent) {
        int statusTemp = intent.getIntExtra("status", -1);

        switch ((statusTemp)) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                tv_Discharging.setText("Unknown");
                imageView.setVisibility(View.GONE);
                gif.setVisibility(View.GONE);
                abc(context, "UNKNOWN", "STATUS UNKNOWN");
                break;

            case BatteryManager.BATTERY_STATUS_CHARGING:
                tv_Discharging.setText("Charging");
                imageView.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
                // getNotificatoin();
                abc(context, "Power", "Charging");
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                tv_Discharging.setText("Discharging");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(intent);
                abc(context, "Power", "Discharging");
                break;

            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                tv_Discharging.setText("Not Charging");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(intent);
                abc(context, "Power", "Not Charging");
                break;


            case BatteryManager.BATTERY_STATUS_FULL:
                tv_Discharging.setText("Battery Full");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(intent);
                break;


            default:
                tv_Discharging.setText("Null");
                imageView.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                getBatteryImage(intent);
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

    private void abc(Context context, String body, String title) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, PI_REQ_CODE, i, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("1", "My Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(body);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "1");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app_icon)
                .setTicker("Hear2")
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(context.getResources().getColor(R.color.colorRed))
                .setContentInfo("Info")
                .setContentIntent(pi);

        notificationManager.notify(0,notificationBuilder.build());
    }








    private void getBatteryImage(Intent intent) {

        int charge = (int) intent.getIntExtra("level", 0);


        if (charge == 0) {
            imageView.setImageResource( R.drawable.batteryzero);

        } else if (charge >0  && charge <=20) {
            imageView.setImageResource( R.drawable.batteryone);

        } else if (charge > 20 && charge <= 40) {
            imageView.setImageResource( R.drawable.batterytwo);

        } else if (charge > 40 && charge <= 60) {
            imageView.setImageResource( R.drawable.batterythree);

        }  else if (charge > 60 && charge <= 80) {
            imageView.setImageResource(R.drawable.batteryfour);
        } else if (charge > 80 && charge <= 100){

            imageView.setImageResource(R.drawable.batteryfive);
        }else{

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



}