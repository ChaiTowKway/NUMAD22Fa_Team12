package edu.northeastern.numad22fa_team12.LocationService;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
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
import edu.northeastern.numad22fa_team12.outfitToday.WardrodeActivity;

import android.location.Location;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;

public class SearchByLocation extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSION_CODE = 99;
    private static final String TAG = "GPS";
//    Double[] latiLoti = new Double[2];
//    LocationRequest locationRequest;
    LocationCallback locationCallback;

    private Map<String, Map>  allUserLocation = new HashMap<>();
    private Map<String, Map> allInfo;
    private double dist;
    private boolean completed = false;
    public String[][] threeFriends;
    public String centerUserId;
    public String centerUserName;

//This activity get the current location of user
    // and update the location into the firebase
    // the location is in the format of map
    // key is laltitue
    //value is longitude
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        Intent nearbyIntent = getIntent();
        centerUserId = nearbyIntent.getStringExtra("centerUserId");
        centerUserName = nearbyIntent.getStringExtra("centerUserName");
        Log.d("noti3", String.valueOf(centerUserName));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);

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
        getAllUserLocation();


    }



//    public void startLocationUpdates() {
//        Log.i(TAG, "an update");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            Log.i(TAG, "need both approval");
////            ActivityCompat.requestPermissions(GPSActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
//            return;
//        }
//        // call get latest location function       how to call        call
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//
//    }

    //just make sure location is not null and user has given GPS permission
    public void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "need both approval");
            ActivityCompat.requestPermissions(SearchByLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }else {
            fusedLocationClient.getCurrentLocation(100,null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("GPS44", "success got location");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
//                                latiLoti[0] = location.getLatitude();
//                                latiLoti[1] = location.getLongitude();

//                                Log.i(TAG, "location1 is " + latiLoti[0] + " : " + latiLoti[1]);
                                Log.i("GPS55", "11111");
                                Log.i("GPS444", "userlocation" + String.valueOf(location.getLatitude()));
                                Log.i("GPS444", "userlocation" + String.valueOf(location.getLongitude()));
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child(centerUserId).child("userInfo");

                                Map<String, Double> userLocation = new HashMap<>();
                                userLocation.put("latitude", location.getLatitude());
                                userLocation.put("longitude", location.getLongitude());

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


    public String[][] getAllUserLocation(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference usersRef = ref.child("OutfitTodayUsers");

        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("RETRIEVE", "Error getting data", task.getException());
                }
                else {
//                    Log.d("RETRIEVE1", String.valueOf(task.getResult().getValue()));
//                    Log.d("RETRIEVE", String.valueOf(task.getResult().getValue().getClass()));
                    allInfo = (Map) task.getResult().getValue();
//                    Log.d("RETRIEVE2", String.valueOf(allInfo));
//                    Log.d("RETRIEVE222", String.valueOf(allInfo.keySet()));
                    for(String userId : allInfo.keySet()) {

                        Log.d("RETRIEVE22", userId);
                        Map<String, Map> details = (Map) allInfo.get(userId).get("userInfo");
                        Log.d("RETRIEVE3", String.valueOf(details));
                        if(details.get("location") != null) {
//                            Log.d("RETRIEVE4", String.valueOf(details.get("location")));
                            allUserLocation.put(userId, details.get("location"));
//                            Log.d("RETRIEVE5", String.valueOf(allUserLocation));
                        }
                    }
//                    Log.d("RETRIEVE6", "final is " + String.valueOf(allUserLocation));
                    getNearby(centerUserId, allUserLocation);
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[0][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[0][1]));
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[1][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[1][1]));
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[2][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[2][1]));

                    //mark the test, need to modify the center user ID

                    // display 3 neaby friends to UI
                    TextView user1 = (TextView)findViewById(R.id.textViewUser1);
                    Button user1Buttonn = findViewById(R.id.check1Btn);
                    if(threeFriends[0][1] == null || threeFriends[0][1].equals("")){
                        user1.setText("");
                        Log.d("friends12", "text " + String.valueOf(threeFriends[0][1]));
                        user1Buttonn.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Log.d("friends12", "text " + String.valueOf(threeFriends[0][1]));
                        user1.setText(threeFriends[0][1]);
                        user1Buttonn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //get the first close userId
                                String ID = threeFriends[0][0];
                                //apply intent
                                Intent i = new Intent(getApplicationContext(), WardrodeActivity.class);
                                i.putExtra("userId", ID);
                                startActivity(i);

                                // connect to notification
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child(threeFriends[0][0]);
                                Log.d("noti1", String.valueOf(usersRef));
//                                DatabaseReference centerUsersRef = ref.child("OutfitTodayUsers").child(centerUserId).child("userInfo");
//                                centerUserName = centerUsersRef.child("userName");

                                Log.d("noti2", String.valueOf(centerUserName));
                                usersRef.child("wardrobeViewBy").setValue(centerUserName);
                            }
                        });

                    }

                    TextView user2 = (TextView)findViewById(R.id.textViewUser2);
                    Button user2Buttonn = findViewById(R.id.check2Btn);
                    Log.d("friends111", "IS NULL?? " + String.valueOf(threeFriends[1][1]));
                    if(threeFriends[1][1] == null || threeFriends[1][1].equals("")){
                        Log.d("friends111", "IS NULL?? " + String.valueOf(threeFriends[1][1]));
                        user2.setText("");
                        user2Buttonn.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Log.d("friends12", "text " + String.valueOf(threeFriends[1][1]));
                        user2.setText(threeFriends[1][1]);
                        user2Buttonn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //get the second close userId
                                String ID = threeFriends[1][0];
                                //apply intent
                                Intent i = new Intent(getApplicationContext(), WardrodeActivity.class);
                                i.putExtra("userId", ID);
                                startActivity(i);

                                // connect to notification
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child(threeFriends[1][0]);
                                Log.d("noti1", String.valueOf(usersRef));

                                Log.d("noti2", String.valueOf(centerUserName));
                                usersRef.child("wardrobeViewBy").setValue(centerUserName);

                            }
                        });
                    }

                    TextView user3 = (TextView)findViewById(R.id.textViewUser3);
                    Button user3Buttonn = findViewById(R.id.check3Btn);
                    if(threeFriends[2][1] == null || threeFriends[2][1].equals("")){
                        user3.setText("");
                        Log.d("friends12", "text " + String.valueOf(threeFriends[2][1]));
                        user3Buttonn.setVisibility(View.INVISIBLE);

                    }else {
                        Log.d("friends12", "text " + String.valueOf(threeFriends[2][1]));
                        user3.setText(threeFriends[2][1]);
                        user3Buttonn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //get the third close userId
                                String ID = threeFriends[2][0];
                                //apply intent
                                Intent i = new Intent(getApplicationContext(), WardrodeActivity.class);
                                i.putExtra("userId", ID);
                                startActivity(i);

                                // connect to notification
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("OutfitTodayUsers").child(threeFriends[2][0]);
                                Log.d("noti1", String.valueOf(usersRef));
//                                DatabaseReference centerUsersRef = ref.child("OutfitTodayUsers").child(centerUserId).child("userInfo");
//                                centerUserName = centerUsersRef.child("userName");

                                Log.d("noti2", String.valueOf(centerUserName));
                                usersRef.child("wardrobeViewBy").setValue(centerUserName);

                            }
                        });

                    }

                }
                completed = true;



            }
        });

//        int count = 0;
//        while (completed == false){
//            count++;
//        }
//        Log.d("RETRIEVE6", String.valueOf(count));
//        completed = false;
//        Log.d("RETRIEVE7", String.valueOf(allUserLocation));
        return threeFriends;
    }

    //get closest 3 friends
    // return a list of list of userId and its username
    public String[][] getNearby(String centerUserId, Map<String, Map> allUserLocations){
        Log.d("friends1", String.valueOf(allUserLocation));
        Log.d("friends4", String.valueOf(allInfo.keySet()));
        PriorityQueue<Pair<String, Double>> pQueue = new PriorityQueue<>((a, b) -> (int)(a.second - b.second));
        for (String userId : allInfo.keySet()){
            if(!userId.equals(centerUserId)) {
                Log.d("friends check", String.valueOf(userId) + String.valueOf(centerUserId));
                dist = getDist(allUserLocations.get(centerUserId), allUserLocations.get(userId));
                Log.d("friends33", String.valueOf(allUserLocations.get(centerUserId)));
                Log.d("friends3", String.valueOf( allUserLocations.get(userId)));
                Log.d("friends2", String.valueOf(dist));
                Pair<String, Double> next = new Pair<>(userId, dist);
                Log.d("friends3", String.valueOf(next));
                pQueue.add(next);
            }
        }

        threeFriends = new String[3][2];
        String resUserId;
        String resUserName;
        for(int i = 0; i < 3; i++){
            Pair<String, Double> res = pQueue.poll();
            resUserId = res.first;
            Map<String, Map> resUserInfo = (Map)allInfo.get(resUserId).get("userInfo");
            resUserName = String.valueOf(resUserInfo.get("userName"));
            threeFriends[i][0] = resUserId;
            threeFriends[i][1] = resUserName;
            Log.d("friends11", "userID " + String.valueOf(resUserId));
            Log.d("friends11", "distance " + String.valueOf(res.second));
        }
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[0][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[0][1]));
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[1][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[1][1]));
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[2][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[2][1]));
        return threeFriends;


    }



    // helper function to calculate the distance betweent 2 location
    public float getDist(Map loc1, Map loc2){
        float[] result = new float[1];
        Log.d("friends5", String.valueOf(loc1.get("latitude")) + String.valueOf(loc1.get("longitude")));

        Log.d("friends6", String.valueOf(loc2));
        Location.distanceBetween((double)loc1.get("latitude"), (double)loc1.get("longitude"), (double)loc2.get("latitude"), (double)loc2.get("longitude"), result);
        Log.d("friends7", String.valueOf(result[0]));
        return result[0];
    }
}