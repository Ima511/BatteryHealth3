package com.example.batteryhealth3;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseBackground extends FirebaseMessagingService {
    MyReceiver br = new MyReceiver();
        public static final String TAG = "MyTag";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: called");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this,FirebaseBackground.class);

        Log.d(TAG, "onMessageReceived: called");

        Log.d(TAG, "onMessageReceived: Message received form: " +remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null){
            br.setNotification(getApplicationContext(),intent);
        }



    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeleteMessages:called");
    }


}
