package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.northeastern.numad22fa_team12.LocationService.SearchByLocation;
import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.databinding.ActivityOutfitTodayBinding;
import edu.northeastern.numad22fa_team12.outfitToday.occasions.MyOccasions;
import edu.northeastern.numad22fa_team12.outfitTodayModel.UserInfo;


public class OutfitToday extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "OutfitTodayActivity";
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private String longitude, latitude;
    private final static int INTERVAL = 60000 * 200;
    private ActivityOutfitTodayBinding binding;
    private Handler mHandler = new Handler();
    private ProgressBar progressBar;
    private String userEmail = "",userEmailKey = "", userName = "";
    private String[] tempData;
    private final  int NOTIFICATION_UNIQUE_ID = 6;
    private static int notificationGeneration = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutfitTodayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = new ProgressBar(OutfitToday.this);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
        userAuth = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) getLocationInfo(location);
                }
            }
        };

        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkPermission();

        new FetchWeatherData().start();

        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myProfile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.add:
                    startActivity(new Intent(OutfitToday.this, AddNewOutfitActivity.class));
                    break;
                case R.id.home:
                    HomeFragment homeFragment2 = new HomeFragment();
                    putDataToBundle(homeFragment2);
                    replaceFragment(homeFragment2);
                    break;
            }
            return true;
        });

        createNotificationChannel();
        getNotificationInfo();
    }

    private void putDataToBundle(Fragment fragment) {
        if (tempData != null) {
            Bundle bundle = new Bundle();
            bundle.putStringArray("tempData", tempData);
            fragment.setArguments(bundle);
            Log.d(TAG, "put the data to bundle");
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: click my profile");
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_myWardrobe:
                Intent WardrobeIntent = new Intent(getApplicationContext(), WardrodeActivity.class);
                WardrobeIntent.putExtra("userId", userEmailKey);
                WardrobeIntent.putExtra("viewMyWardrobe", true);
                startActivity(WardrobeIntent);
                break;
            case R.id.button_myOccasions:
                Intent myOccasionsIntent = new Intent(this, MyOccasions.class);
                startActivity(myOccasionsIntent);
                break;
            case R.id.button_myOutfitSuggestion:
                Intent myOutfitSuggestionIntent = new Intent(this, OutfitToday.class);
                startActivity(myOutfitSuggestionIntent);
                break;
            case R.id.button_nearbyOutfits:
                Intent nearbyIntent = new Intent(getApplicationContext(), SearchByLocation.class);
                nearbyIntent.putExtra("centerUserId", userEmailKey);
//                userName = (String) userRef.child(userEmailKey).child("userInfo").child("userName");
//                UserInfo userInfo = task.getResult().getValue(UserInfo.class);
//                userName = userInfo.getUserName();
                userRef.child(userEmailKey).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                            Log.e(TAG, "Error getting data", task.getException());
                        } else {
                            UserInfo userInfo = task.getResult().getValue(UserInfo.class);
                            userName = userInfo.getUserName();
                            Log.d("noti4", String.valueOf(userName));
                            nearbyIntent.putExtra("centerUserName", userName);
                            startActivity(nearbyIntent);

                        }
                    }
                });


                Log.d("noti5", String.valueOf(userEmailKey));

                break;
            case R.id.button_updateMyProfile:
                Log.d(TAG, "onClick: update my profile");
                Intent updateProfileIntent = new Intent(this, UpdateProfile.class);
                startActivity(updateProfileIntent);
                break;
            case R.id.button_logOut:
                userAuth.signOut();
                Toast.makeText(OutfitToday.this, "Sign out successfully!",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(OutfitToday.this, NewUserRegisterActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Dialog backConfirmDialog = new Dialog(this);
        backConfirmDialog.setContentView(R.layout.back_confirm_dialog_layout);
        final Button cancelBtn = backConfirmDialog.findViewById(R.id.terminateCancelBtn),
                terminateBtn = backConfirmDialog.findViewById(R.id.terminateOkBtn);
        backConfirmDialog.setCanceledOnTouchOutside(true);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backConfirmDialog.dismiss();
            }
        });
        terminateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        backConfirmDialog.show();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getLocationInfo(location);
                        new FetchWeatherData().start();
                    } else {
                        startLocationUpdates();
                    }
                }
            });
        } else {
            requestPermission();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    // get the longitude and latitude and pass to weather api
    private void getLocationInfo(Location location){
        double newLongitude = location.getLongitude(), newLatitude = location.getLatitude();
        longitude = String.valueOf(newLongitude);
        latitude = String.valueOf(newLatitude);
        updateLocation(newLongitude, newLatitude);
    };

    private void updateLocation(double newLongitude, double newLatitude) {
        userRef.child(userEmailKey).child("userInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Update location fail: ", task.getException());
                }
                else {
                    Log.d(TAG, "Update location successfully: ");
                    userRef.child(userEmailKey).child("userInfo").child("location").child("latitude").setValue(newLatitude);
                    userRef.child(userEmailKey).child("userInfo").child("location").child("longitude").setValue(newLongitude);
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(OutfitToday.this,
                        "Location access granted", Toast.LENGTH_SHORT).show();
                checkPermission();} else {
                Toast.makeText(OutfitToday.this,
                        "Location access denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchWeatherData extends Thread{
        String data = "";

        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            if (longitude != null && latitude != null) {
                String urlBasedOnLocation = String.format("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,rain&temperature_unit=fahrenheit", latitude, longitude);
                Log.d(TAG, "url is: " + urlBasedOnLocation);

                try {
                    URL url = new URL(urlBasedOnLocation);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        data += line;
                    }

                    if (!data.isEmpty()){
                        JSONObject jsonObject = new JSONObject(data).getJSONObject("hourly");
                        JSONArray temp;
                        temp = jsonObject.getJSONArray("temperature_2m");

                        List<Double> tempList = new ArrayList<>();

                        for(int i = 0; i < temp.length(); i++) {
                            tempList.add(temp.getDouble(i));
                        }

                        Log.d(TAG, "temp list: " + tempList.toString());

                        double maxTemp = Collections.max(tempList), minTemp = Collections.min(tempList);
                        double avgTemp = 0;
                        for (double t : tempList){
                            avgTemp += t;
                        }
                        avgTemp = avgTemp / tempList.size();
                        Log.d(TAG, String.format("maxT: %f, minT: %f, avgT: %f", maxTemp, minTemp, avgTemp));
                        tempData = new String[]{String.valueOf(minTemp), String.valueOf(maxTemp), String.valueOf(avgTemp)};
                        HomeFragment homeFragment = new HomeFragment();
                        putDataToBundle(homeFragment);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                        Log.d(TAG, "start the home fragment");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
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
                    if (wardrobeViewBy != null) {
                        sendNotification(wardrobeViewBy);
                        userRef.child(userEmailKey).child("wardrobeViewBy").setValue("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}