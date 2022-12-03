package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;

public class WardrodeActivity extends AppCompatActivity {
    ArrayList<Outfit> outfits = new ArrayList<>();
    String id = "1";
    RecyclerView wardrobeRecycler;
    private WardrobeAdapter wardrobeAdapter;
    public FirebaseDatabase database;
    public DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        db = database.getReference("outfit");
        if(savedInstanceState == null){
            this.outfits = new ArrayList<>();
        }else{
            onRestoreInstanceState(savedInstanceState);
        }
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("userId");
        }
        setContentView(R.layout.activity_wardrode);

        wardrobeRecycler = findViewById(R.id.wardrobe_recylerview);
        this.wardrobeAdapter = new WardrobeAdapter(this, outfits);
        wardrobeRecycler.setLayoutManager(new LinearLayoutManager(this));
        wardrobeRecycler.setAdapter(wardrobeAdapter);
        wardrobeRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Query query = db.orderByChild("userId").equalTo(id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outfits.clear();
                for(DataSnapshot snap: snapshot.getChildren()){

                    Outfit t = snap.getValue(Outfit.class);
                    outfits.add(t);
                    System.out.println(1);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("outfit" , outfits);
        outState.putString("id" , id);
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        outfits = savedInstanceState.getParcelableArrayList("outfit");
        id = savedInstanceState.getString("id");


    }



}