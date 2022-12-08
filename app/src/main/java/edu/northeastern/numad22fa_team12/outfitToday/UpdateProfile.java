package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.numad22fa_team12.LocationService.SearchByLocation;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.UserInfo;


import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;



public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UpdateProfile";
    private FirebaseDatabase database;
    private FirebaseAuth userAuth;
    private DatabaseReference userRef;
    private TextView userEmailTV, userNameTV, contactNumberTV;
    private Button getLocation, updateProfile;
    private String userEmail = "",userEmailKey = "", userName = "", contactNumber = "", location = "";

    private final  int NOTIFICATION_UNIQUE_ID = 6;
    private static int notificationGeneration = 1;

    public LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 50000;
    private double latitude;
    private double longitude;
    private Location mLocation;
    private TextView latitudeTV, longitudeTV;
    FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSION_CODE = 99;
    LocationRequest locationRequest;
    LocationCallback locationCallback;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        createNotificationChannel();
        userEmailTV = findViewById(R.id.textView_UserEmail);
        userNameTV = findViewById(R.id.editText_Name);
        contactNumberTV = findViewById(R.id.editTextPhone);
        getLocation = findViewById(R.id.button_getLocation);
        getLocation = findViewById(R.id.button_updateProfile);

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
            Log.e("userId", userEmailKey);

        }
        getCurrUserInfo();
        getNotificationInfo();

//        checkPermission();
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        createLocationRequest();
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    try {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location == null) continue;
                }
            }
        };
                    // to make sure user give permission to access GPS ; Otherwise, ask user for that permission
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
                            }
                        }
                );

                    // call locationPermissionRequest
                    locationPermissionRequest.launch(new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    });
        latitudeTV = findViewById(R.id.text_latitude);
        longitudeTV = findViewById(R.id.text_logitude);
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

                    if (userInfo.getLocation().get("latitude") != null) {
                        latitudeTV.setText(userInfo.getLocation().get("latitude").toString());
                    }
                    if (userInfo.getLocation().get("longitude") != null) {
                        longitudeTV.setText(userInfo.getLocation().get("longitude").toString());
                    }
                    Log.d(TAG, "onComplete: lat" + userInfo.getLocation().get("latitude"));
                }
            }
        });
    }

    public void updateCurrentUserInfo() {
        userName = userNameTV.getText().toString();
        contactNumber = contactNumberTV.getText().toString();
//        try {
//            latitude = Double.parseDouble(latitudeTV.getText().toString());
//            longitude = Double.parseDouble(longitudeTV.getText().toString());
//        } catch (Exception e) {
//            Log.d(TAG, "updateCurrentUserInfo: no valid location");
//        }

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

                    userInfo.setUserName(userName);
                    userInfo.setContactNumber(contactNumber);
                    HashMap<String, Double> location = new HashMap<>();
                    location.put("latitude", latitude);
                    location.put("longitude", longitude);
                    userInfo.setLocation(location);
                    userRef.child(userEmailKey).child("userInfo").setValue(userInfo);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
//        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_cancel:
                Toast.makeText(UpdateProfile.this, "Profile update cancelled!",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
                startActivity(intent);
                break;
            case R.id.button_updateProfile:
                updateCurrentUserInfo();
                Toast.makeText(UpdateProfile.this, "Profile updated successfully!",
                        Toast.LENGTH_LONG).show();
                Intent backIntent = new Intent(UpdateProfile.this, OutfitToday.class);
                startActivity(backIntent);
                break;
            case R.id.button_getLocation:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UpdateProfile.this, "No location granted!",
                            Toast.LENGTH_LONG).show();
                } else {

                    Log.i(TAG, "check before");
                    updateGPS();

                    for(int i=0; i < 5;i++){
                        Log.i(TAG, "Test" + i);
                        updateGPS();
                    }

                    Toast.makeText(UpdateProfile.this, "Current location updated!",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
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
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.register);
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

//    private void createLocationRequest() {
//        LocationRequest.Builder lrb = new LocationRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS);
//        lrb.setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        lrb.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest = lrb.build();
//    }

    public void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "need both approval");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }else {
            fusedLocationClient.getCurrentLocation(100,null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            Log.i(TAG, "success got location");
                            // Got last known location. In some rare situations this can be null.
                            if (loc != null) {
                                // Logic to handle location object

//                                Log.i(TAG, "location1 is " + latiLoti[0] + " : " + latiLoti[1]);
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child(userEmailKey).child("userInfo");

                                Map<String, Double> userLocation = new HashMap<>();
                                userLocation.put("latitude", loc.getLatitude());
                                userLocation.put("longitude", loc.getLongitude());
                                Log.i("GPS444", "userlocation" + String.valueOf(loc.getLatitude()));
                                Log.i("GPS444", "userlocation" + String.valueOf(loc.getLongitude()));

                                usersRef.child("location").setValue(userLocation);
                                Log.i("GPS555", "child " + usersRef);
//                                Log.i("GPS555", "userlocation" + userLocation.get("latitude"));
//                                Log.i("GPS555", "userlocation" + userLocation.get("longitude"));
//                                Log.i(TAG, "final location is " + latiLoti[0] + " : " + latiLoti[1]);
                                Log.i("GPS555", "location:" + loc.getProvider());
//                                getLastLocation();
                                latitudeTV.setText(String.valueOf(loc.getLatitude()));
                                longitudeTV.setText(String.valueOf(loc.getLongitude()));
                            }
                        }
                    });
        }
    }

    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                                latitudeTV.setText(String.valueOf(latitude));
                                longitudeTV.setText(String.valueOf(longitude));
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }


    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateProfile.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
    }
}