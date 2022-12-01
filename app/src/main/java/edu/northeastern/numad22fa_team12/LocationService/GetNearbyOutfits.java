package edu.northeastern.numad22fa_team12.LocationService;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.*;
public class GetNearbyOutfits  {
    private Map<String, Map>  allUserLocation;

    //  public Map<String, Map> getAllUserLocation()
    public void getAllUserLocation(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference usersRef = ref.child("OutfitTodayUsers");

        usersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("RETRIEVE", "Error getting data", task.getException());
                }
                else {
                    Log.d("RETRIEVE", String.valueOf(task.getResult().getValue()));
                }
            }
        });


    }

//    //get closest 3 friends
//    public List<List<String>> getNearby(String userId, Map<String, Map> allUserLocations){
//
//    }
//
//    public double getDist(double Dist1, double Dist2){
//
//    }

}
