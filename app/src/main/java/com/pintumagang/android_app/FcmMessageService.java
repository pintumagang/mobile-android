package com.pintumagang.android_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pintumagang.android_app.entity.User;
import com.pintumagang.android_app.fragment.NotifikasiService;

public class FcmMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //onMessageReceived will be called when ever you receive new message from server.. (app in background and foreground )

        showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        int id_user = user.getId();
        NotifikasiService notiService = new NotifikasiService(getApplicationContext(),id_user, remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));

        notiService.notifikasiDbInsert();

        if (remoteMessage.getData().size() > 0) {
            Log.d("aa", "Message data payload: " + remoteMessage.getData());
        }

// Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d("aa", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

    }

    private void showNotification(String title, String message){
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.pintumagang);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setSound(defaultSoundUri)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}