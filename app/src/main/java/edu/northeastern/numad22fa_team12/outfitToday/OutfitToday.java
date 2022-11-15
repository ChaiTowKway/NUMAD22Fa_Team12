package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Friend;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Item;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OutfitTodayUser;
import edu.northeastern.numad22fa_team12.webservice.WebServiceActivity;

public class OutfitToday extends AppCompatActivity implements View.OnClickListener{
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_today);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_adduser:
                OutfitTodayUser newUser = new OutfitTodayUser();
                userRef.child("User1").setValue(newUser);
//                Item newItem = new Item();
//                userRef.child("User1").child("wardrobe").child("url").setValue(newItem);
//                long millis = System.currentTimeMillis();
//                java.util.Date date = new java.util.Date(millis);
//                Friend newFriend = new Friend("User2", String.valueOf(date));
//                userRef.child("User1").child("friends").child("User2").setValue(newFriend);
                break;
        }
    }
}
