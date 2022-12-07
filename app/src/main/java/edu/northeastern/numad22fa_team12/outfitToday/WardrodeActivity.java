package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad22fa_team12.MainActivity;
import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;

public class WardrodeActivity extends AppCompatActivity {
    private static final String TAG = "wardrobeActivity";
    ArrayList<Outfit> outfits = new ArrayList<>();
    RecyclerView wardrobeRecycler;
    private WardrobeAdapter wardrobeAdapter;
    public FirebaseDatabase database;
    public DatabaseReference db;
    private String userId;
    private boolean viewMyWardrobe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();

        if(savedInstanceState == null){
            this.outfits = new ArrayList<>();
        }else{
            onRestoreInstanceState(savedInstanceState);
        }
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getString("userId");
            viewMyWardrobe = extras.getBoolean("viewMyWardrobe");
        }
        Log.d(TAG, userId);
        Log.d(TAG, String.valueOf(viewMyWardrobe));

        setContentView(R.layout.activity_wardrode);

        db = database.getReference().child("outfit");

        wardrobeRecycler = findViewById(R.id.wardrobe_recylerview);
        this.wardrobeAdapter = new WardrobeAdapter(this, outfits, viewMyWardrobe);
        wardrobeRecycler.setLayoutManager(new LinearLayoutManager(this));
        wardrobeRecycler.setAdapter(wardrobeAdapter);
        wardrobeRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Query query = db.orderByChild("userId").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outfits.clear();
                for(DataSnapshot snap: snapshot.getChildren()){

                    Outfit t = snap.getValue(Outfit.class);
                    outfits.add(t);
                    Log.d(TAG, outfits.toString());
                }
                if (outfits.isEmpty()) {
                    Toast.makeText(WardrodeActivity.this,
                            "No outfit in the wardrobe!", Toast.LENGTH_SHORT).show();
                }
               wardrobeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), OutfitToday.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("outfit" , outfits);
        outState.putString("id" , userId);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        outfits = savedInstanceState.getParcelableArrayList("outfit");
        userId = savedInstanceState.getString("id");
    }

}