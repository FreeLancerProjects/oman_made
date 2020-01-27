package com.technology.circles.apps.omanmade.notification;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technology.circles.apps.omanmade.preferences.Preferences;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.newInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> map = remoteMessage.getData();

        for (String key:map.keySet())
        {
            Log.e("key",key+"    value "+map.get(key));
        }




    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNewNotificationVersion(map);
        }else
            {
                createOldNotificationVersion(map);

            }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {



    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map)
    {



    }








}
