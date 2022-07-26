package com.example.batteryhealth3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView
            tv_TimeLeftValue,
            tv_AvailabilityValue,
            tv_NominalVolValue,
            tv_ConditionValue,
            tv_tempValue,
            tv_TechnologyName,
            tv_cycleCountValue;
    ImageView imageView;
    MyReceiver myReceiver = new MyReceiver();
    IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_TimeLeftValue = (TextView) findViewById(R.id.tv_TimeLeftValue);
        tv_AvailabilityValue = (TextView) findViewById(R.id.tv_ChargingSourceValue);
        tv_NominalVolValue = (TextView) findViewById(R.id.tv_NominalVolValue);
        tv_ConditionValue = (TextView) findViewById(R.id.tv_ConditionValue);
        tv_tempValue = (TextView) findViewById(R.id.tv_tempValue);
        tv_TechnologyName = (TextView) findViewById(R.id.tv_TechnologyName);
        tv_cycleCountValue = (TextView) findViewById(R.id.tv_cycleCountValue);

        imageView = (ImageView) findViewById(R.id.imageView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // step1
        setSupportActionBar(toolbar);
        // text as title
        getSupportActionBar().setTitle("Battery Health");
        //        toolbar.setTitle("Battery Fiction");

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(myReceiver, intentfilter);

    }





//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        this.registerReceiver(myReceiver, intentFilter);
//
//         }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        this.unregisterReceiver(myReceiver);
//
//    }
}