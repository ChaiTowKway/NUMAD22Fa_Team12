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
    private Map<String, Map>  allUserLocation = new HashMap<>();
    private Map<String, Map> allInfo;

    //  public Map<String, Map> getAllUserLocation()
    public Map<String, Map> getAllUserLocation(){
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
                    Log.d("RETRIEVE1", String.valueOf(task.getResult().getValue()));
//                    Log.d("RETRIEVE", String.valueOf(task.getResult().getValue().getClass()));
                    allInfo = (Map) task.getResult().getValue();
                    for(String userId : allInfo.keySet()) {
                        Log.d("RETRIEVE2", userId);
                        Map<String, Map> details = (Map) allInfo.get(userId).get("userInfo");
                        Log.d("RETRIEVE3", String.valueOf(details));
                        if(details.get("location") != null) {
                            Log.d("RETRIEVE4", String.valueOf(details.get("location")));
                            allUserLocation.put(userId, details.get("location"));
                        }
                    }
                }
            }
        });
        Log.d("RETRIEVE5", String.valueOf(allUserLocation));
    return allUserLocation;
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
