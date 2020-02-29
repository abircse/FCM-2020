package com.coxtunes.firebasefcm.notification;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.coxtunes.firebasefcm.MessageShowActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessegingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("FCM Token", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (!remoteMessage.getData().isEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    /**
     *  Handle Data Message
     */
    private void handleDataMessage(JSONObject jsonObject) {

        try {
            JSONObject object = jsonObject.getJSONObject("data");
            String title = object.getString("title");
            String message = object.getString("message");
            String imageUrl = object.getString("image");
            String timestamp = object.getString("timestamp");

            JSONObject payload = object.getJSONObject("payload");
            String articleData = payload.getString("article_data");

            Intent intent = new Intent(getApplicationContext(), MessageShowActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("timestamp", timestamp);
            intent.putExtra("article_data", articleData);
            intent.putExtra("image", imageUrl);

            ////// check for image attachment//////////
            if (TextUtils.isEmpty(imageUrl))
            {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, intent);
            }
            else
            {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, intent, imageUrl);
            }
            ////////////////////////////////////////////

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context applicationContext, String title, String message, String timestamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(applicationContext);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timestamp,intent,null);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context applicationContext, String title, String message, String timestamp, Intent intent, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(applicationContext);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timestamp,intent,imageUrl);
    }

}

