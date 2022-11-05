package edu.northeastern.numad22fa_team12.stickItToEm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.numad22fa_team12.R;

public class notificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        setContentView(R.layout.activity_notification);
    }

    public void createNotificationChannel() {

    }

    public void sendNotification(View view) {

    }
}
