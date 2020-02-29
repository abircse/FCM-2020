package com.coxtunes.firebasefcm.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.Connection;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.coxtunes.firebasefcm.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

class NotificationUtils {

    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showNotificationMessage(String title, String message, String timestamp, Intent intent, String imageUrl)
    {
        if (TextUtils.isEmpty(message))
            return;

        // notification icon
        int icon = R.mipmap.ic_launcher;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Configuration.DEFAULT_NOTIFICATION_CHANNEL_ID);
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notification");
        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches())
            {
               // Call getBitmapFromUrl
               Bitmap bitmap = getBitmapFromURL(imageUrl);
               if (bitmap!=null)
               {
                   showBigNotification(bitmap, mBuilder, icon, title, message, timestamp, pendingIntent, alarmSound);
               }
               else
               {
                   showSmallNotification(mBuilder, icon, title, message, timestamp, pendingIntent, alarmSound);
               }
            }
        }
        else
        {
            showSmallNotification(mBuilder, icon, title, message, timestamp, pendingIntent, alarmSound);
            playNotificationSound();
        }
    }

    /*show Big Notification Method*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timestamp, PendingIntent pendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        Notification notification = mBuilder.setSmallIcon(icon)
                .setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timestamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Configuration.DEFAULT_NOTIFICATION_CHANNEL_ID, "Firebase Notification channel for sample app", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(Configuration.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /*show Small Notification Method*/
    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timestamp, PendingIntent pendingIntent, Uri alarmSound) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        Notification notification = mBuilder.setSmallIcon(icon)
                .setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timestamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Configuration.DEFAULT_NOTIFICATION_CHANNEL_ID, "Firebase Notification channel for sample app", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(Configuration.NOTIFICATION_ID, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private Bitmap getBitmapFromURL(String imageUrl)
    {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * Play Notification Sound
    * */
    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notification");
            Ringtone ringtone = RingtoneManager.getRingtone(context,alarmSound);
            ringtone.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Get Current Time in Milisecond
    * */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private long getTimeMilliSec(String timestamp)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timestamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



}
