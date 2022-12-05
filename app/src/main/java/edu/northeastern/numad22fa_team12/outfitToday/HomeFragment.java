package edu.northeastern.numad22fa_team12.outfitToday;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionsList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView minMaxTempTv, avgTempTv, occasionTv, warmthTv;
    public ImageView warmthImage, topImage, bottomImage, shoeImage;
    public String minTemp, maxTemp, avgTemp;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private String userEmail = "",userEmailKey = "";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
        userAuth = FirebaseAuth.getInstance();
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }
        getCurrUserInfo();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        minMaxTempTv = (TextView) view.findViewById(R.id.minMaxTemp);
        avgTempTv = (TextView) view.findViewById(R.id.avgTemp);
        occasionTv = (TextView) view.findViewById(R.id.curOccasion);

        warmthTv = (TextView) view.findViewById(R.id.todayWarmth);
        warmthImage = (ImageView) view.findViewById(R.id.warmthImage);
        topImage = (ImageView) view.findViewById(R.id.topImage);
        bottomImage = (ImageView) view.findViewById(R.id.bottomImage);
        shoeImage = (ImageView) view.findViewById(R.id.shoeImage);
        setWeatherTextViewImageView();
        return view;
    }

    @SuppressLint("DefaultLocale")
    public void setWeatherTextViewImageView() {
        if (getArguments() != null && getArguments().getStringArray("tempData") != null) {
            String[] tempData = getArguments().getStringArray("tempData");
            minTemp = tempData[0];
            maxTemp = tempData[1];
            avgTemp = tempData[2];
            Log.d(TAG, String.format("maxT: %s, minT: %s, avgT: %s", maxTemp, minTemp, avgTemp));
            if (minMaxTempTv != null && avgTempTv != null) {
                minMaxTempTv.setText(String.format("L: %s°F / H: %s°F", minTemp, maxTemp));
                avgTempTv.setText(String.format("Today's temperature: %.2f°F", Float.valueOf(avgTemp)));
            }
            if (warmthTv != null && warmthImage != null) {
                // set the warmthTV and image based on the current temperature
                int tempInInt = Math.round(Float.parseFloat(avgTemp));
                if (tempInInt <= 50) {
                    warmthTv.setText("Today is cold!");
                    warmthImage.setImageResource(R.drawable.cold);
                } else if (tempInInt > 90) {
                    warmthTv.setText("Today is hot!");
                    warmthImage.setImageResource(R.drawable.hot);
                } else {
                    warmthTv.setText("Today is warm!");
                    warmthImage.setImageResource(R.drawable.warm);
                }
            }
        }
    }

    public void setOccasionTextView(String curOccasion) {
        if (occasionTv != null) {
            occasionTv.setText(curOccasion);
        }
    }
    public void getCurrUserInfo() {
        userRef.child(userEmailKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                }
                else {
                    OccasionsList occasionsList = task.getResult().child("occasionsList").getValue(OccasionsList.class);
                    if (occasionsList != null && occasionsList.checkOccasion() != "No occasion configured") {
                        String curOccasion = occasionsList.checkOccasion();
                        Log.d(TAG, "current occasion: " + curOccasion);
                        setOccasionTextView("Occasion based on your setting: " + curOccasion);
                    } else {
                        Log.d(TAG, "No occasion found!");
                        setOccasionTextView("No occasion found, add occasions for better suggestion!");
                    }
                    // TODO: The categoryList has to be generated manually
//                    CategoryList categoryList = task.getResult().child("categoryList").getValue(CategoryList.class);

                }
            }
        });
    }

}