package com.example.batteryhealth3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {

    TextView
            tv_TimeLeftValue,
            tv_AvailabilityValue,
            tv_NominalVolValue,
            tv_ConditionValue,
            tv_tempValue,
            tv_TechnologyName,
            tv_cycleCountValue;
    ImageView imageView;

    public MyReceiver() {
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        tv_TimeLeftValue = (TextView) ((Activity)context).findViewById(R.id.tv_TimeLeftValue);
        

        String actionString = intent.getAction();
        Toast.makeText(context, actionString, Toast.LENGTH_SHORT).show();


        // Are we charging / charged?
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        // Display whatever the state in the form of a Toast
        if(isCharging) {
            Toast.makeText(context, "Battery is charging ", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Battery is not charging ", Toast.LENGTH_SHORT).show();
        }

        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
        tv_TimeLeftValue.setText(String.valueOf(intent.getIntExtra("level",0)) + "%");
        }

    }


}