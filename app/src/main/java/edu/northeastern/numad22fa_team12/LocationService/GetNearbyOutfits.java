package edu.northeastern.numad22fa_team12.LocationService;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

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
    private double dist;
    private boolean completed = false;

    //  get all user location info from DB
    // store user location info in a hashmap
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
//                    Log.d("RETRIEVE1", String.valueOf(task.getResult().getValue()));
//                    Log.d("RETRIEVE", String.valueOf(task.getResult().getValue().getClass()));
                    allInfo = (Map) task.getResult().getValue();
                    for(String userId : allInfo.keySet()) {
//                        Log.d("RETRIEVE2", userId);
                        Map<String, Map> details = (Map) allInfo.get(userId).get("userInfo");
//                        Log.d("RETRIEVE3", String.valueOf(details));
                        if(details.get("location") != null) {
//                            Log.d("RETRIEVE4", String.valueOf(details.get("location")));
                            allUserLocation.put(userId, details.get("location"));
//                            Log.d("RETRIEVE5", String.valueOf(allUserLocation));
                        }
                    }
//                    Log.d("RETRIEVE6", "final is " + String.valueOf(allUserLocation));
                    getNearby("sc@email-com", allUserLocation);
                }
                completed = true;



            }
        });

//        int count = 0;
//        while (completed == false){
//            count++;
//        }
//        Log.d("RETRIEVE6", String.valueOf(count));
//        completed = false;
//        Log.d("RETRIEVE7", String.valueOf(allUserLocation));
        return allUserLocation;
    }

    //get closest 3 friends
    // return a list of list of userId and its username
    public String[][] getNearby(String centerUserId, Map<String, Map> allUserLocations){
        Log.d("friends1", String.valueOf(allUserLocation));
        Log.d("friends4", String.valueOf(allInfo.keySet()));
        PriorityQueue<Pair<String, Double>> pQueue = new PriorityQueue<>((a, b) -> (int)(a.second - b.second));
        for (String userId : allInfo.keySet()){
            if(userId != centerUserId) {
                dist = getDist(allUserLocations.get(centerUserId), allUserLocations.get(userId));
                Log.d("friends2", String.valueOf(dist));
                Pair<String, Double> next = new Pair<>(userId, dist);
                Log.d("friends3", String.valueOf(next));
                pQueue.add(next);
            }
        }

        String[][] result = new String[3][2];
        String resUserId;
        String resUserName;
        for(int i = 0; i < 3; i++){
            Pair<String, Double> res = pQueue.poll();
            resUserId = res.first;
            Map<String, Map> resUserInfo = (Map)allInfo.get(resUserId).get("userInfo");
            resUserName = String.valueOf(resUserInfo.get("userName"));
            result[i][0] = resUserId;
            result[i][1] = resUserName;
        }
        Log.d("friends8", "final res " + String.valueOf(result[0][0]));
        Log.d("friends9", "final res " + String.valueOf(result[0][1]));
        Log.d("friends8", "final res " + String.valueOf(result[1][0]));
        Log.d("friends9", "final res " + String.valueOf(result[1][1]));
        Log.d("friends8", "final res " + String.valueOf(result[2][0]));
        Log.d("friends9", "final res " + String.valueOf(result[2][1]));
        return result;


    }



    // helper function to calculate the distance betweent 2 location
    public float getDist(Map loc1, Map loc2){
        float[] result = new float[1];
        Log.d("friends5", String.valueOf(loc1.get("latitude")) + String.valueOf(loc1.get("longitude")));

        Log.d("friends6", String.valueOf(loc2));
        Location.distanceBetween((double)loc1.get("latitude"), (double)loc1.get("longitude"), (double)loc2.get("latitude"), (double)loc2.get("longitude"), result);
        Log.d("friends7", String.valueOf(result[0]));
        return result[0];
    }

}
