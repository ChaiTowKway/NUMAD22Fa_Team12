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
// this class get closes 3 nearby users
// this class has 3 fun
//1 getAllUserLocation
//2 getNearby
//3 getDist(Map loc1, Map loc2)
public class GetNearbyOutfits  {
    private Map<String, Map>  allUserLocation = new HashMap<>();
    private Map<String, Map> allInfo;
    private double dist;
    private boolean completed = false;
    public String[][] threeFriends;

    //  get all user location info from DB
    // store user location info in a hashmap
    //public Map<String, Map> getAllUserLocation(){
    public String[][] getAllUserLocation(){
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
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[0][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[0][1]));
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[1][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[1][1]));
                    Log.d("friends8", "final res " + String.valueOf(threeFriends[2][0]));
                    Log.d("friends9", "final res " + String.valueOf(threeFriends[2][1]));

                    //mark the test, need to modify the center user ID
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
        return threeFriends;
    }

    //get closest 3 friends
    // return a list of list of userId and its username
    public String[][] getNearby(String centerUserId, Map<String, Map> allUserLocations){
        Log.d("friends1", String.valueOf(allUserLocation));
        Log.d("friends4", String.valueOf(allInfo.keySet()));
        PriorityQueue<Pair<String, Double>> pQueue = new PriorityQueue<>((a, b) -> (int)(a.second - b.second));
        for (String userId : allInfo.keySet()){
            if(!userId.equals(centerUserId)) {
                Log.d("friends check", String.valueOf(userId) + String.valueOf(centerUserId));
                dist = getDist(allUserLocations.get(centerUserId), allUserLocations.get(userId));
                Log.d("friends2", String.valueOf(dist));
                Pair<String, Double> next = new Pair<>(userId, dist);
                Log.d("friends3", String.valueOf(next));
                pQueue.add(next);
            }
        }

        threeFriends = new String[3][2];
        String resUserId;
        String resUserName;
        for(int i = 0; i < 3; i++){
            Pair<String, Double> res = pQueue.poll();
            resUserId = res.first;
            Map<String, Map> resUserInfo = (Map)allInfo.get(resUserId).get("userInfo");
            resUserName = String.valueOf(resUserInfo.get("userName"));
            threeFriends[i][0] = resUserId;
            threeFriends[i][1] = resUserName;
        }
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[0][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[0][1]));
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[1][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[1][1]));
//        Log.d("friends8", "final res " + String.valueOf(threeFriends[2][0]));
//        Log.d("friends9", "final res " + String.valueOf(threeFriends[2][1]));
        return threeFriends;


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

//helper fundtion to get private instance variable
//    public String[][] getThreeFriends(){
////        Log.d("friends8", "getThreeFriends " + String.valueOf(threeFriends[0][0]));
////        Log.d("friends9", "getThreeFriends " + String.valueOf(threeFriends[0][1]));
////        Log.d("friends8", "getThreeFriends " + String.valueOf(threeFriends[1][0]));
////        Log.d("friends9", "getThreeFriends " + String.valueOf(threeFriends[1][1]));
////        Log.d("friends8", "getThreeFriends " + String.valueOf(threeFriends[2][0]));
////        Log.d("friends9", "getThreeFriends " + String.valueOf(threeFriends[2][1]));
//        return threeFriends;
//    }

}



//
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>



//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>



//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>



//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>


//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>


//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>

//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>
//
//    <?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:app="http://schemas.android.com/apk/res-auto"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        tools:context=".LocationService.SearchByLocation">
//
//<TextView
//        android:id="@+id/textViewTitle"
//                android:layout_width="264dp"
//                android:layout_height="49dp"
//                android:text="Here are your 3 nearby friends!"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold"
//                app:layout_constraintBottom_toTopOf="@+id/check1Btn"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.496"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.497" />
//
//<TextView
//        android:id="@+id/textViewUser1"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User1"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser2"
//                app:layout_constraintEnd_toStartOf="@+id/check1Btn"
//                app:layout_constraintHorizontal_bias="0.247"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.832" />
//
//<TextView
//        android:id="@+id/textViewUser2"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User2"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toTopOf="@+id/textViewUser3"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.243"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.88" />
//
//<TextView
//        android:id="@+id/textViewUser3"
//                android:layout_width="115dp"
//                android:layout_height="47dp"
//                android:text="User3"
//                android:textAlignment="center"
//                android:textColor="@color/mainColor"
//                android:textSize="20sp"
//                android:textStyle="bold|italic"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toStartOf="@+id/check2Btn"
//                app:layout_constraintHorizontal_bias="0.324"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.692" />
//
//<Button
//        android:id="@+id/check1Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.926"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.405" />
//
//<Button
//        android:id="@+id/check2Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.933"
//                app:layout_constraintStart_toStartOf="parent"
//                app:layout_constraintTop_toTopOf="parent"
//                app:layout_constraintVertical_bias="0.556" />
//
//<Button
//        android:id="@+id/check3Btn"
//                android:layout_width="169dp"
//                android:layout_height="55dp"
//                android:backgroundTint="@color/mainColor"
//                android:text="check wardrobe"
//                android:textSize="15sp"
//                app:layout_constraintBottom_toBottomOf="parent"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintHorizontal_bias="0.802"
//                app:layout_constraintStart_toEndOf="@+id/textViewUser3"
//                app:layout_constraintTop_toBottomOf="@+id/check2Btn"
//                app:layout_constraintVertical_bias="0.142" />
//
//</androidx.constraintlayout.widget.ConstraintLayout>