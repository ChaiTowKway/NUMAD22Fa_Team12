package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WebServiceActivity extends AppCompatActivity {
    Button searchButton;
    EditText inputBrand;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        searchButton = findViewById(R.id.search_button);
        inputBrand = findViewById(R.id.input_brand);
    }



}