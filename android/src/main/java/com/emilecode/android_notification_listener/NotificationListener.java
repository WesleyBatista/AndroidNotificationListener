package com.emilecode.android_notification_listener;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Set;

public class NotificationListener extends NotificationListenerService{


    public static String NOTIFICATION_INTENT = "notification_event";
    public static String NOTIFICATION_PACKAGE_NAME = "package_name";
    public static String NOTIFICATION_PACKAGE_MESSAGE = "package_message";
    public static String NOTIFICATION_PACKAGE_TEXT = "package_text";
    public static String NOTIFICATION_PACKAGE_EXTRA = "package_extra";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        try{
            // Retrieve package name to set as title.
            String packageName = sbn.getPackageName();
            // Retrieve extra object from notification to extract payload.
            Bundle extras = sbn.getNotification().extras;
            String packageMessage = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
            String packageText = extras.getCharSequence("android.title").toString();
            String packageExtra = convertBumbleToJsonString(sbn.getNotification().extras);
            //Log.d("converted Json", packageExtra);
            // Pass data from one activity to another.
            Intent intent = new Intent(NOTIFICATION_INTENT);
            intent.putExtra(NOTIFICATION_PACKAGE_NAME, packageName);
            intent.putExtra(NOTIFICATION_PACKAGE_MESSAGE, packageMessage);
            intent.putExtra(NOTIFICATION_PACKAGE_TEXT, packageText);
            intent.putExtra(NOTIFICATION_PACKAGE_EXTRA, packageExtra);
            sendBroadcast(intent);
        }
        catch (Exception error)
        {
            Log.w( "Crashing aborded ", "An exception occured, I do not know yet what causes that error\nIt seams that a bundle is null on a notification received or it is just a bug\nIf you did not receive the notification please raise a complain on my github" );
        }



    }

    private String convertBumbleToJsonString(Bundle extra)
    {
        JSONObject json = new JSONObject();
        Set<String> keys = extra.keySet();
        for (String key : keys) {
            try {

                json.put(key, JSONObject.wrap(extra.get(key)));
            } catch(JSONException e) {
                //Handle exception here
            }
        }

        return json.toString();
    }
}
