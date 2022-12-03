package edu.northeastern.numad22fa_team12.LocationService;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.numad22fa_team12.R;

public class SearchByLocation extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSION_CODE = 99;
    private static final String TAG = "GPS";
//    Double[] latiLoti = new Double[2];
    LocationRequest locationRequest;
    LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference();
////                    DatabaseReference usersRef = ref.child("OutfitTodayUsers").child("testing2@gmail-com").child("userInfo");
//
////                    String[] latiLongti = {String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())};
//        Map<String, String> userLocation = new HashMap<>();
//        userLocation.put("latitude", "1");
//        userLocation.put("longtitude", "2");
//
////                    usersRef.child("location").setValue(userLocation);
//        ref.setValue(userLocation);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location == null) continue;
                    // Update UI with location data
                    // ...
                    //final version
//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference ref = database.getReference();
//                    DatabaseReference usersRef = ref.child("OutfitTodayUsers").child("testing2@gmail-com").child("userInfo");
//
//                    Map<String, Double> userLocation = new HashMap<>();
//                    userLocation.put("latitude", location.getLatitude());
//                    userLocation.put("longtitude", location.getLongitude());
//
//                    usersRef.child("location").setValue(userLocation);
//                    ref.setValue(userLocation);


//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference();
//                    DatabaseReference usersRef = ref.child("OutfitTodayUsers").child("testing2@gmail-com").child("userInfo");

//                    String[] latiLongti = {String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())};
//        Map<String, Double> userLocation = new HashMap<>();
//        userLocation.put("latitude", 3.12);
//        userLocation.put("longtitude", 4.55);
//
////                    usersRef.child("location").setValue(userLocation);
//        ref.setValue(userLocation);


//                    Log.i(TAG, "child " + ref);
//                    Log.i(TAG, "userlocation" + userLocation.get("latitude"));
//                    Log.i(TAG, "update" + location.getLatitude() + " : " + location.getLongitude());
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
                                ActivityCompat.requestPermissions(SearchByLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
                            }
                        }
                );

        // call locationPermissionRequest
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        Log.i(TAG, "check before");
        updateGPS();

        for(int i=0; i < 5;i++){
            Log.i(TAG, "Test" + i);
            updateGPS();
        }



//        startLocationUpdates();
    }

    public void startLocationUpdates() {
        Log.i(TAG, "an update");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "need both approval");
//            ActivityCompat.requestPermissions(GPSActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
            return;
        }
        // call get latest location function       how to call        call
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    //just make sure location is not null and user has given GPS permission
    public void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "need both approval");
            ActivityCompat.requestPermissions(SearchByLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i(TAG, "success got location");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
//                                latiLoti[0] = location.getLatitude();
//                                latiLoti[1] = location.getLongitude();

//                                Log.i(TAG, "location1 is " + latiLoti[0] + " : " + latiLoti[1]);
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child("test1@gmail-com").child("userInfo");

                                Map<String, Double> userLocation = new HashMap<>();
                                userLocation.put("latitude", location.getLatitude());
                                userLocation.put("longtitude", location.getLongitude());

                                usersRef.child("location").setValue(userLocation);
//                                Log.i(TAG, "child " + ref);
//                                Log.i(TAG, "userlocation" + userLocation.get("latitude"));
//                                Log.i(TAG, "final location is " + latiLoti[0] + " : " + latiLoti[1]);
                                Log.i(TAG, "location:" + location.getProvider());
                            }
                        }
                    });
        }
    }
}