package edu.northeastern.numad22fa_team12.stickItToEm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.northeastern.numad22fa_team12.R;

public class StickItToEmActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "StickItToEmActivity";
    private static final String SERVER_KEY = "AAAAn-7LHFg:APA91bFjKqRC26hJV6TSDwVKKmw2frTjAWPBne9-" +
            "SDObmZhYxgZDBNUOvqN4uYIap1flKY-VMT5NhSfpJ2prSPOEJ2L7_ucxU2ybsayIlExHbfcbUVrOMEL56DzbNhy3yTPTkVq8NHUl";
    private static String FCM_REGISTRATION_TOKEN;
    private FirebaseDatabase database;
    private FirebaseAuth userAuth;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter stickerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int[] stickersLocations;
    private int stickerSelected;
    private ImageView imageSelected;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);
        imageSelected = findViewById(R.id.imageView_test);
        createRecycleView();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            String errorMsg = "Failed to get registration token " + task.getException();
                            Log.e(TAG, errorMsg);
                            Toast.makeText(StickItToEmActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        FCM_REGISTRATION_TOKEN = task.getResult();
                        String msg = "Registration Token: " + FCM_REGISTRATION_TOKEN;
                        Log.d(TAG, msg);
                    }
                });

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser curUser = userAuth.getCurrentUser();
//        if (curUser == null) {
//            // if user not register, take user to register page
//            startActivity(new Intent(StickItToEmActivity.this, RegisterActivity.class));
//        }
//    }

    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    private void createRecycleView() {
        stickersLocations = new int[]{R.drawable.cat, R.drawable.dog, R.drawable.duck, R.drawable.hedgehog,
                R.drawable.koala, R.drawable.panda, R.drawable.pig, R.drawable.rooster};
        stickerSelected = R.drawable.cat;
        recyclerView = findViewById(R.id.RecycleView_Stickers);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        stickerAdapter = new StickerAdapter(this, stickersLocations, imageSelected);
        recyclerView.setAdapter(stickerAdapter);
    }

    public void sendMessage() {
        stickerSelected = (int) getIntent().getSerializableExtra("id");
        Log.i(TAG, "STICK ID" + stickerSelected);
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }
}