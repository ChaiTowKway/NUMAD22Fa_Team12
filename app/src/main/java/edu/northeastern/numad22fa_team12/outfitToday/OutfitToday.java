package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class OutfitToday extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_today);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
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
}
