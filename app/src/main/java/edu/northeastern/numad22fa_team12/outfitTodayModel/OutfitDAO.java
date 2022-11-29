package edu.northeastern.numad22fa_team12.outfitTodayModel;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutfitDAO {
    public FirebaseDatabase database;
    public DatabaseReference db;
    public List<Outfit> result;

    public OutfitDAO(){
        database = FirebaseDatabase.getInstance();
        db = database.getReference("outfit");
        result = new ArrayList<>();
    }


    public void getOutfitsBySeason(String userId , SeasonEnum season){
        Query query = db.orderByChild("userId").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snap: snapshot.getChildren()){

                    Outfit t = snap.getValue(Outfit.class);
                    result.add(t);

                }

                result =  result.stream().filter(p->p.getSeasonId() == season.value).collect(Collectors.toList());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println(error);
            }
        });


    }





}
