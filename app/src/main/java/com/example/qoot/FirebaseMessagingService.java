package com.example.qoot;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        String messageTitle=remoteMessage.getNotification().getTitle();
        String messageBody=remoteMessage.getNotification().getBody();

        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody);

        int mNotificatonId=(int) System.currentTimeMillis();
        NotificationManager mNotifyMgr=
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificatonId,mBuilder.build());



    }
}
