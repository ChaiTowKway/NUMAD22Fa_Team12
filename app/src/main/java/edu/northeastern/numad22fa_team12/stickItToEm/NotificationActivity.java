package edu.northeastern.numad22fa_team12.stickItToEm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;

public class NotificationActivity extends AppCompatActivity {
    private final  int NOTIFICATION_UNIQUE_ID = 7;
    private static int notificationGeneration = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        createNotificationChannel();
        setContentView(R.layout.activity_notification);

    }

    public void testBut(View view) {
        createNotificationChannel();
        sendNotification(view);
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
//            String description = getString(R.string.channel_description);
            String description = "Description";
            int importannce = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Sticker_Channel", name, importannce);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    public void sendNotification(View view, Integer stickerID, String sentByUser){
//
//
//        Intent intent = new Intent(this, MainActivity.class);
//
//        PendingIntent readIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_MUTABLE);
//
//        String channelID = "Sticker_Channel";
//
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), stickerID);
//        Notification noti = new NotificationCompat.Builder(this, channelID)
//                .setContentTitle("New message" + Integer.toString(notificationGeneration++) )
//                .setContentText("From" + sentByUser).setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setTicker("Ticker text")
//                .setLargeIcon(bm)
//
//                .addAction(R.drawable.ic_launcher_foreground, "Read", readIntent)
//                .setContentIntent(readIntent).build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(NOTIFICATION_UNIQUE_ID + notificationGeneration, noti);
//    }
    public void sendNotification(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);

        PendingIntent readIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_MUTABLE);

        String channelID = "Sticker_Channel";

        Bitmap bm = BitmapFactory.decodeResource(getResources(), 2131165306);
        Notification noti = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("New message" + Integer.toString(notificationGeneration++) )
                .setContentText("Subject Text").setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Ticker text")
                .setLargeIcon(bm)

                .addAction(R.drawable.ic_launcher_foreground, "Read", readIntent)
                .setContentIntent(readIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_UNIQUE_ID + notificationGeneration, noti);
    }
}
