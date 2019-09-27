package com.computer.android.tabianconsulting.utility;

import android.util.Log;

import com.computer.android.tabianconsulting.AdminActivity;
import com.computer.android.tabianconsulting.ChatActivity;
import com.computer.android.tabianconsulting.ChatroomActivity;
import com.computer.android.tabianconsulting.LoginActivity;
import com.computer.android.tabianconsulting.R;
import com.computer.android.tabianconsulting.RegisterActivity;
import com.computer.android.tabianconsulting.SettingsActivity;
import com.computer.android.tabianconsulting.SignedInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    // Getting Firebase Message Token
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "onNewToken: New Token: " + token);

        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        String notificationBody = "";
//        String notificationTitle = "";
//        String notificationData = "";
//
//        try {
//            notificationData = remoteMessage.getData().toString();
//            notificationTitle = remoteMessage.getNotification().getTitle();
//            notificationBody = remoteMessage.getNotification().getBody();
//        } catch (NullPointerException e) {
//            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage());
//        }
//        Log.d(TAG, "onMessageReceived: data: " + notificationData);
//        Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
//        Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);

        // init image loader since that will be the first code that executes if they click a notification
        initImageLoader();

        String identifyDataType = remoteMessage.getData().get(getString(R.string.data_type));
        // SITUATION: Application is in foreground then only send priority notifications such as an admin notification
        if (isApplicationInForeground()) {
            if (identifyDataType.equals(getString(R.string.data_type_admin_broadcast))) {
                // build admin broadcast notification

            }
        }

        // SITUATION: Application is in background or closed
        else if (!isApplicationInForeground()) {
            if (identifyDataType.equals(getString(R.string.data_type_admin_broadcast))) {
                // build admin broadcast notification

            } else if (identifyDataType.equals(getString(R.string.data_type_chat_message))) {
                // build chat message notification

            }
        }
    }

    private boolean isApplicationInForeground() {
        // check all the activities to see if any of them are running
        boolean isActivityRunning = SignedInActivity.isActivityRunning
                || ChatActivity.isActivityRunning || LoginActivity.isActivityRunning
                || ChatroomActivity.isActivityRunning || AdminActivity.isActivityRunning
                || RegisterActivity.isActivityRunning || SettingsActivity.isActivityRunning;
        if (isActivityRunning) {
            Log.d(TAG, "isApplicationInForeground: application is in foreground.");
            return true;
        }
        Log.d(TAG, "isApplicationInForeground: application is in background or closed.");
        return false;
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(getString(R.string.dbnode_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.field_messaging_token))
                .setValue(token);
    }
}
