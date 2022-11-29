package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.List;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.databinding.ActivityOutfitTodayBinding;
import edu.northeastern.numad22fa_team12.outfitToday.occasions.MyOccasions;


public class OutfitToday extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "OutfitTodayActivity";
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private String longitude, latitude;
    private final static int INTERVAL = 50000;
//    ActivityMainBinding binding;
    private ActivityOutfitTodayBinding binding;
    private Handler mHandler = new Handler();
    private ProgressBar progressBar;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private String userEmail = "",userEmailKey = "", userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutfitTodayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        replaceFragment(homeFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myProfile:
                    replaceFragment(profileFragment);
                    break;
                case R.id.add:
                    startActivity(new Intent(OutfitToday.this, AddNewOutfitActivity.class));
                    break;
                case R.id.home:
                    replaceFragment(homeFragment);
                    break;
            }
            return true;
        });

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

        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser curUser = userAuth.getCurrentUser();
//        if (curUser == null) {
//            // if user not register, take user to register page
//            startActivity(new Intent(OutfitToday.this, NewUserRegisterActivity.class));
//        }
//    }

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
            case R.id.button_setting:
                break;
            case R.id.button_myWardrobe:
                break;
            case R.id.button_myOccasions:
                Intent myOccasionsIntent = new Intent(this, MyOccasions.class);
                startActivity(myOccasionsIntent);
                break;
            case R.id.button_myNotification:
                break;
            case R.id.button_myOutfitSuggestion:
                break;
            case R.id.button_nearbyOutfits:
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
                        new fetchWeatherData().start();
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
        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());
    };

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

    class fetchWeatherData extends Thread{
        String data = "";

        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar = new ProgressBar(OutfitToday.this);
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
//                        homeFragment.updateMinMaxTv(String.format("Min/Max temperature: %f / %f (°F)", minTemp, maxTemp));
//                        homeFragment.updateAvgTv(String.format("Average temperature: %f (°F)", avgTemp));
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

}