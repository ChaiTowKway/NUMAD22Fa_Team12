package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.northeastern.numad22fa_team12.outfitToday.AddNewOutfitActivity;
import edu.northeastern.numad22fa_team12.outfitToday.NewUserRegisterActivity;
import edu.northeastern.numad22fa_team12.outfitToday.OutfitToday;
import edu.northeastern.numad22fa_team12.outfitToday.WardrodeActivity;
import edu.northeastern.numad22fa_team12.stickItToEm.RegisterActivity;
import edu.northeastern.numad22fa_team12.webservice.WebServiceActivity;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        int buttonID = view.getId();
        switch (buttonID) {
            case R.id.button_web_service:
                startActivity(new Intent(MainActivity.this, WebServiceActivity.class));
                break;
            case R.id.button_StickItToEm:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.button_About:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.button_OutfitToday:
                startActivity(new Intent(MainActivity.this, NewUserRegisterActivity.class));
                break;
        }
    }
}