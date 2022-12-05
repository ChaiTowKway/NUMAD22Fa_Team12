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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.CategoryEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OutfitDAO;
import edu.northeastern.numad22fa_team12.outfitTodayModel.SeasonEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Item;

public class AddNewOutfitActivity extends AppCompatActivity {
    Button imageAddButton;
    ImageView outfitImageView;
    Button submitButton;
    Uri image_uri;
    String web_uri;
    private StorageReference ref = FirebaseStorage.getInstance().getReference();
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Spinner occasionSpinner;
    Spinner seasonSpinner;
    Spinner categorySpinner;
    int occasionId = 0;
    int seasonId = 0;
    int categoryId = 0;
    OutfitDAO dao;
    String userId = "1";
    public FirebaseDatabase database;
    public DatabaseReference db;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    Outfit outfit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_outfit);
        if(getIntent().getExtras() != null){
            outfit = getIntent().getExtras().getParcelable("outfit");
        }

        dao = new OutfitDAO();
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

        database = FirebaseDatabase.getInstance();

        userRef = database.getReference().child("OutfitTodayUsers");
        userAuth = FirebaseAuth.getInstance();
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userId = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }

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
        if(outfit != null){
            Picasso.get().load(outfit.getUrl()).into(outfitImageView);
            seasonSpinner.setSelection(outfit.getSeasonId());
            occasionSpinner.setSelection(outfit.getOccasionId());
            categorySpinner.setSelection(outfit.getCategoryId());
            seasonId = outfit.getSeasonId();
            occasionId = outfit.getOccasionId();
            categoryId = outfit.getOccasionId();
        }

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

        submitButton = findViewById(R.id.outfitAddSubmitButton1);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageId = String.valueOf(UUID.randomUUID());
                Outfit outfit = new Outfit(categoryId,web_uri,imageId,userId ,seasonId,occasionId );
                database.getReference("outfit").child(imageId).setValue(outfit);

                String occasion = OccasionEnum.values()[outfit.getOccasionId()].toString();
                String category = CategoryEnum.values()[outfit.getCategoryId()].toString();
                String season = SeasonEnum.values()[outfit.getSeasonId()].toString();

                Item item = new Item(seasonId, occasionId, categoryId, web_uri);
                userRef.child(userId).child("wardrobe").child(imageId).setValue(item);
                userRef.child(userId).child("categoryList").child(category).child("occasion").child(occasion).child(imageId).setValue(web_uri);
                userRef.child(userId).child("categoryList").child(category).child("season").child(season).child(imageId).setValue(web_uri);
                userRef.child(userId).child("categoryList").child(category).child("season").child("all").child(imageId).setValue(web_uri);
                finish();

            }
        });
        submitButton.setEnabled(false);

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
            final StorageReference reff = ref.child(System.currentTimeMillis()+"."+String.valueOf(image_uri));

            reff.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            web_uri = String.valueOf(uri);
                            submitButton.setEnabled(true);
                        }
                    });
                }
            });
            outfitImageView.setImageURI(image_uri);
        }
    }
}