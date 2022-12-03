package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;

public class WardrodeActivity extends AppCompatActivity {
    List<Outfit> outfits = new ArrayList<>();
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("userId");
        }
        setContentView(R.layout.activity_wardrode);


    }
}