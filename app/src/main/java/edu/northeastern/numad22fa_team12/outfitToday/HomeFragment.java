package edu.northeastern.numad22fa_team12.outfitToday;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.CategoryList;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionsList;
import edu.northeastern.numad22fa_team12.outfitTodayModel.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView minMaxTempTv, avgTempTv;
    public String minTemp, maxTemp, avgTemp;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private String userEmail = "",userEmailKey = "";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        minMaxTempTv = (TextView) view.findViewById(R.id.minMaxTemp);
        avgTempTv = (TextView) view.findViewById(R.id.avgTemp);
        setHomePageTextView();
        return view;
    }

    @SuppressLint("DefaultLocale")
    public void setHomePageTextView() {
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
                    // TODO: The categoryList has to be generated manually
//                    CategoryList categoryList = task.getResult().child("categoryList").getValue(CategoryList.class);
                    Log.d(TAG, "data get successfully");

                }
            }
        });
    }

}