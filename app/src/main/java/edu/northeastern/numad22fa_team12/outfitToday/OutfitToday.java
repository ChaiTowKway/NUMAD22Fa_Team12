package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Friend;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Item;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OutfitTodayUser;
import edu.northeastern.numad22fa_team12.stickItToEm.RegisterActivity;
import edu.northeastern.numad22fa_team12.stickItToEm.StickItToEmActivity;
import edu.northeastern.numad22fa_team12.webservice.WebServiceActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class OutfitToday extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private final static int INTERVAL = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_today);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");

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

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_myProfile:
                startActivity(new Intent(OutfitToday.this, MyProfile.class));
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
        String longitude, latitude;
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


}
