package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                startActivity(new Intent(MainActivity.this, StickItToEmActivity.class));
                break;
            case R.id.button_About:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
    }
}