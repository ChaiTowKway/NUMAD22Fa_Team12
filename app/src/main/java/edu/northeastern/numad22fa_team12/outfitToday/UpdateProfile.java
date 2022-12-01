package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;
import edu.northeastern.numad22fa_team12.outfitTodayModel.UserInfo;
import edu.northeastern.numad22fa_team12.stickItToEm.StickItToEmActivity;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UpdateProfile";
    private FirebaseDatabase database;
    private FirebaseAuth userAuth;
    private DatabaseReference userRef;
    private TextView userEmailTV, userNameTV, contactNumberTV, locationTV;
    private Button getLocation, updateProfile;
    private String userEmail = "",userEmailKey = "", userName = "", contactNumber = "", location = "";

    private final  int NOTIFICATION_UNIQUE_ID = 6;
    private static int notificationGeneration = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        createNotificationChannel();
        userEmailTV = findViewById(R.id.textView_UserEmail);
        userNameTV = findViewById(R.id.editText_Name);
        contactNumberTV = findViewById(R.id.editTextPhone);
        locationTV = findViewById(R.id.editText_UserLocation);
        getLocation = findViewById(R.id.button_getLocation);
        getLocation = findViewById(R.id.button_updateProfile);

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }
        getCurrUserInfo();
        getNotificationInfo();
    }

    public void getCurrUserInfo() {
        userRef.child(userEmailKey).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    UserInfo userInfo = task.getResult().getValue(UserInfo.class);
                    userEmailTV.setText(userInfo.getEmail());
                    userNameTV.setText(userInfo.getUserName());
                    contactNumberTV.setText(userInfo.getContactNumber());
                    locationTV.setText(userInfo.getLocation());
                }
            }
        });
    }

    public void updateCurrentUserInfo() {
        userName = userNameTV.getText().toString();
        contactNumber = contactNumberTV.getText().toString();
        location = locationTV.getText().toString();

        userRef.child(userEmailKey).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    UserInfo userInfo = task.getResult().getValue(UserInfo.class);
                    if (userName == null) {
                        userName = "";
                    }
                    if (contactNumber == null) {
                        contactNumber = "";
                    }
                    if (location == null) {
                        location = "";
                    }
                    userInfo.setUserName(userName);
                    userInfo.setContactNumber(contactNumber);
                    userInfo.setLocation(location);
                    userRef.child(userEmailKey).child("userInfo").setValue(userInfo);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_cancel:
                Toast.makeText(UpdateProfile.this, "Profile update cancelled!",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.button_updateProfile:
                updateCurrentUserInfo();
                Toast.makeText(UpdateProfile.this, "Profile updated successfully!",
                        Toast.LENGTH_LONG).show();
                break;
        }
        Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
        startActivity(intent);
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
            String description = "Description";
            int importanceDefault = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("OutfitTodayChannel", name, importanceDefault);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String wardrobeViewBy){
        Intent intent = new Intent(this, OutfitToday.class);
        PendingIntent readIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_MUTABLE);

        String channelID = "OutfitTodayChannel";
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outfit_profile);
        Notification noti = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("OutfitToday")
                .setContentText(wardrobeViewBy + " is viewing your wardrobe now!")
                .setSmallIcon(R.drawable.appicon)
                .setTicker("Ticker text")
                .setLargeIcon(bm)
                .addAction(R.drawable.ic_launcher_foreground, "Open OutfitToday", readIntent)
                .setContentIntent(readIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_UNIQUE_ID, noti);
    }

    private void getNotificationInfo() {
        userRef.child(userEmailKey).child("wardrobeViewBy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!Objects.equals(snapshot.getValue(String.class), "")) {
                    String wardrobeViewBy = snapshot.getValue(String.class);
                    sendNotification(wardrobeViewBy);
                    userRef.child(userEmailKey).child("wardrobeViewBy").setValue("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}