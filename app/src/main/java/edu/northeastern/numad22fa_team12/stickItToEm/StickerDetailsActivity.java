package edu.northeastern.numad22fa_team12.stickItToEm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import edu.northeastern.numad22fa_team12.model.Sticker;

public class StickerDetailsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String curUser;
    private List<Sticker> stickerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            curUser = auth.getCurrentUser().getUid().toString();
            ref = database.getReference().child(curUser);
        }

    }
}
