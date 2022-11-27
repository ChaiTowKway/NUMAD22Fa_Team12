package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.User;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UpdateProfile";
    private FirebaseDatabase database;
    private FirebaseAuth userAuth;
    private DatabaseReference userRef;
    private TextView userEmailTV, userNameTV, contactNumberTV, locationTV;
    private Button getLocation, updateProfile;
    private String userEmail = "",userEmailKey = "", userName = "", contactNumber = "", location = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

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

    }

    public void getCurrUserInfo() {
        userRef.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    userEmailTV.setText(userEmail);
                }
            }
        });
    }

    public void updateCurrentUserInfo() {
        userName = userNameTV.getText().toString();
        contactNumber = contactNumberTV.getText().toString();
        location = locationTV.getText().toString();

        userRef.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    if (userName == null) {
                        userName = "";
                    }
                    if (contactNumber == null) {
                        contactNumber = "";
                    }
                    if (location == null) {
                        location = "";
                    }
                    userRef.child(userEmailKey).child("userInfo").child("userName").setValue(userName);
                    userRef.child(userEmailKey).child("userInfo").child("contactNumber").setValue(contactNumber);
                    userRef.child(userEmailKey).child("userInfo").child("location").setValue(location);
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
            case R.id.button_signOutProfile:
                userAuth.signOut();
                Toast.makeText(UpdateProfile.this, "Sign out successfully!",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfile.this, NewUserRegisterActivity.class));
                break;
            case R.id.button_updateProfile:
                updateCurrentUserInfo();
                Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
                startActivity(intent);
                break;
        }
    }
}