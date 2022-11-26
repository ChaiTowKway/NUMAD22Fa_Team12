package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

public class UpdateProfile extends AppCompatActivity {

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
        userNameTV = findViewById(R.id.editText_username);
        contactNumberTV = findViewById(R.id.editTextPhone);
        locationTV = findViewById(R.id.editText_UserLocation);
        getLocation = findViewById(R.id.button_getLocation);
        getLocation = findViewById(R.id.button_updateProfile);

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        userRef = database.getReference().child("OutfitUsers");
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
                    userName = userRef.child(userEmailKey).child("UserInfo").child("userName").get().toString();
                    userNameTV.setText(userName);
                    Log.d("firebase email", String.valueOf(userAuth.getCurrentUser().getEmail()));
                }
            }
        });
    }

    public void updateCurrentUserInfo() {
        userName = userNameTV.getText().toString();
        contactNumber = contactNumberTV.getText().toString();
        location = locationTV.getText().toString();
        userRef.child(userEmailKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    return;
                }
                userRef.child(userEmailKey).child("UserInfo").child("userName").setValue(userName);
                userRef.child(userEmailKey).child("UserInfo").child("contactNumber").setValue(contactNumber);
                userRef.child(userEmailKey).child("UserInfo").child("location").setValue(location);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateProfile.this, OutfitToday.class);
        startActivity(intent);
    }
}