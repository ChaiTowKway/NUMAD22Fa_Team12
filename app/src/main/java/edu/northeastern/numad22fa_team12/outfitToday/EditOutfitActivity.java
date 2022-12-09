package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.CategoryEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Item;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionEnum;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OutfitDAO;
import edu.northeastern.numad22fa_team12.outfitTodayModel.SeasonEnum;

public class EditOutfitActivity extends AppCompatActivity {

    Button imageAddButton, itemDeleteButton;
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
    OutfitDAO dao;
    String userId = "1";
    Outfit outfit;
    String web_uri = "";
    public FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private StorageReference ref = FirebaseStorage.getInstance().getReference();
    private String oldOccasion, oldSeason, oldCategory, oldUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outfit);
        if(getIntent().getExtras() != null){
            outfit = getIntent().getExtras().getParcelable("outfit");
            oldOccasion = OccasionEnum.values()[outfit.getOccasionId()].toString();
            oldCategory = CategoryEnum.values()[outfit.getCategoryId()].toString();
            oldSeason = SeasonEnum.values()[outfit.getSeasonId()].toString();
            oldUri = outfit.getUrl();
        }
        dao = new OutfitDAO();
        imageAddButton = findViewById(R.id.image_edit_button2);
        outfitImageView = findViewById(R.id.outfit_image_view2);
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

        submitButton = findViewById(R.id.outfitAddSubmitButton2);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (web_uri.isEmpty()) {
                    String id = outfit.getItemId();
                    String userId = outfit.getUserId();
                    Outfit newOutfit = new Outfit(categoryId, oldUri, id, userId, seasonId, occasionId);
                    database.getReference("outfit").child(id).setValue(newOutfit);

                    String newOccasion = OccasionEnum.values()[newOutfit.getOccasionId()].toString();
                    String newCategory = CategoryEnum.values()[newOutfit.getCategoryId()].toString();
                    String newSeason = SeasonEnum.values()[newOutfit.getSeasonId()].toString();

                    Item item = new Item(seasonId, occasionId, categoryId, oldUri);

                    userRef.child(userId).child("wardrobe").child(id).setValue(item);

                    // remove the old category
                    userRef.child(userId).child("categoryList").child(oldCategory).child("occasion").child(oldOccasion).child(id).removeValue();
                    userRef.child(userId).child("categoryList").child(oldCategory).child("season").child(oldSeason).child(id).removeValue();

                    // add it to the new category
                    userRef.child(userId).child("categoryList").child(newCategory).child("occasion").child(newOccasion).child(id).setValue(oldUri);
                    userRef.child(userId).child("categoryList").child(newCategory).child("season").child(newSeason).child(id).setValue(oldUri);
                } else {
                    String id = outfit.getItemId();
                    String userId = outfit.getUserId();
                    Outfit newOutfit = new Outfit(categoryId, web_uri, id, userId, seasonId, occasionId);
                    database.getReference("outfit").child(id).setValue(newOutfit);

                    String newOccasion = OccasionEnum.values()[newOutfit.getOccasionId()].toString();
                    String newCategory = CategoryEnum.values()[newOutfit.getCategoryId()].toString();
                    String newSeason = SeasonEnum.values()[newOutfit.getSeasonId()].toString();

                    Item item = new Item(seasonId, occasionId, categoryId, web_uri);

                    userRef.child(userId).child("wardrobe").child(id).setValue(item);

                    // remove the old category
                    userRef.child(userId).child("categoryList").child(oldCategory).child("occasion").child(oldOccasion).child(id).removeValue();
                    userRef.child(userId).child("categoryList").child(oldCategory).child("season").child(oldSeason).child(id).removeValue();

                    // add it to the new category
                    userRef.child(userId).child("categoryList").child(newCategory).child("occasion").child(newOccasion).child(id).setValue(web_uri);
                    userRef.child(userId).child("categoryList").child(newCategory).child("season").child(newSeason).child(id).setValue(web_uri);
                }

                Toast.makeText(EditOutfitActivity.this,
                        "Outfit has been updated", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
//        submitButton.setEnabled(false);

        // delete item
        itemDeleteButton = findViewById(R.id.outfitDeleteButton);
        itemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog deleteConfirmDialog = new Dialog(EditOutfitActivity.this);
                deleteConfirmDialog.setContentView(R.layout.delete_confirm_dialog_layout);
                final Button cancelBtn = deleteConfirmDialog.findViewById(R.id.deleteCancelBtn),
                        deleteBtn = deleteConfirmDialog.findViewById(R.id.deleteOkBtn);
                deleteConfirmDialog.setCanceledOnTouchOutside(true);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteConfirmDialog.dismiss();
                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String outfitId = outfit.getItemId();
                        userRef.child(userId).child("wardrobe").child(outfitId).removeValue();

                        // remove the old category
                        userRef.child(userId).child("categoryList").child(oldCategory).child("occasion").child(oldOccasion).child(outfitId).removeValue();
                        userRef.child(userId).child("categoryList").child(oldCategory).child("season").child(oldSeason).child(outfitId).removeValue();
                        database.getReference("outfit").child(outfitId).removeValue();

                        Toast.makeText(EditOutfitActivity.this,
                                "Outfit has been deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                deleteConfirmDialog.show();
            }
        });

        occasionSpinner = findViewById(R.id.occasion_spinner2);
        seasonSpinner = findViewById(R.id.season_spinner2);
        categorySpinner = findViewById(R.id.category_spinner2);
        String[] seasonList = {"spring","summer","fall","winter"};
        String[] occasionList = {"casual",
                "formal",
                "sports",
                "work"};
        String[] categoryList = { "bottoms",
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
            Picasso.get().load(outfit.getUrl()).rotate(90).into(outfitImageView);
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
//                            submitButton.setEnabled(true);
                        }
                    });
                }
            });
            outfitImageView.setImageURI(image_uri);
        }
    }
}