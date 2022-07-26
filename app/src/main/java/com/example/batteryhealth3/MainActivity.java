package com.example.batteryhealth3;

import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    int id;

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


    private String CHANNEL_ID = "Battery Channel";
//     Notification Codes

    private static final int NOTIFICATION_ID = 2;
    private static final int PI_REQ_CODE = 100;
    SharedPreferences sharedpreferences;
    String autoStart;

    private  static final String TAG = "message-med";


    // Notification Codes

//    private static final String CHANNEL_ID = "Battery Channel";
//    private static final int NOTIFICATION_ID = 2;
//    private static final int PI_REQ_CODE = 100;


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

        reference = database.getInstance().getReference().child("iAmUser");


        // step1
        setSupportActionBar(toolbar);
        // text as title
        getSupportActionBar().setTitle("Battery Health");
        //        toolbar.setTitle("Battery Fiction");

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(myReceiver, intentfilter);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        autoStart = sharedpreferences.getString("autoStart", "");
        if (autoStart.equals("")) {
            AutoStartHelper.getInstance().getAutoStartPermission(this);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println( "Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println(token);

                        Toast.makeText(MainActivity.this, "Your device registration token is:- "+token, Toast.LENGTH_SHORT).show();

                        Log.d(token,"token");
                    }
                });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    id = (int) snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        this.unregisterReceiver(myReceiver);
//
//    }

//    public void getNotification() {
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.app_icon, null);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap largeIcon = bitmapDrawable.getBitmap();
//
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification_type1;
//
//        Intent inotify = new Intent(getApplicationContext(),MainActivity.class);
//        inotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//        PendingIntent pi = PendingIntent.getBroadcast(this,
//                PI_REQ_CODE,
//                inotify,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        // Big Picture Style
//
//
//        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
//                .bigPicture(((BitmapDrawable) (ResourcesCompat.getDrawable(getResources(), R.drawable.app_icon, null))).getBitmap())
//                .bigLargeIcon(largeIcon)
//                .setBigContentTitle("Battery Message")
//                .setSummaryText("message arrived");
//
//        // Inbox style
//        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
//                .addLine("A")
//                .addLine("B")
//                .addLine("C")
//                .addLine("D")
//                .addLine("E")
//                .addLine("F")
//                .addLine("G")
//                .addLine("H")
//                .addLine("I")
//                .addLine("J")
//                .addLine("K")
//                .setBigContentTitle("Full Message")
//                .setSummaryText("Message from Battery Manager");
//
//
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            notification_type1 = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_icon)
//                    .setContentText("New Message Type 1")
//                    .setSubText("new message  from BatteryHealth")
//                    .setContentIntent(pi)
//                    .setStyle(bigPictureStyle)
//                    .setChannelId(CHANNEL_ID)
//                    .build();
//            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
//
//        } else {
//
//            notification_type1 = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.app_icon)
//                    .setContentText("New Message Type 1")
//                    .setSubText("new message from human")
//                    .setStyle(bigPictureStyle)
//                    .setContentIntent(pi)
//                    .build();
//        }
//        nm.notify(NOTIFICATION_ID, notification_type1);
//
//
//    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        this.unregisterReceiver(myReceiver);
//           }
}

