package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;
public class AddNewOutfitActivity extends AppCompatActivity {
    Button imageAddButton;
    ImageView outfitImageView;
    Button submitButton;
    Uri image_uri;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Spinner occasionSpinner;
    Spinner seasonSpinner;
    Spinner categorySpinner;
    int occasionId = 0;
    int seasonId = 0;
    int categoryId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_outfit);
        imageAddButton = findViewById(R.id.image_edit_button);
        outfitImageView = findViewById(R.id.outfit_image_view);
        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }else{
                        openCamera();
                    }
                }else{
                    openCamera();
                }
            }
        });

        occasionSpinner = findViewById(R.id.occasion_spinner);
        seasonSpinner = findViewById(R.id.season_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
      String[] seasonList = {"spring","summer","fall","winter"};
      String[] occasionList = {"casual",
              "formal",
              "sports",
              "work"};
      String[] categoryList = { "buttoms",
              "shoes",
              "tops"};
        ArrayAdapter seasonAdapter = new ArrayAdapter(this,
               R.layout.spinner_layout,seasonList);
        ArrayAdapter occasionAdapter = new ArrayAdapter(this,
                R.layout.spinner_layout,occasionList);
        ArrayAdapter categoryAdapter = new ArrayAdapter(this,
                R.layout.spinner_layout,categoryList);

        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonAdapter);
        occasionSpinner.setAdapter(occasionAdapter);
        categorySpinner.setAdapter(categoryAdapter);

        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    seasonId = i;
                    System.out.println(seasonId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        occasionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                occasionId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore. Images.Media.TITLE, "New Pict");
        values.put(MediaStore. Images.Media.DESCRIPTION, "From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    // Hand permission result
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_CAPTURE_CODE){
            outfitImageView.setImageURI(image_uri);
        }
    }
}