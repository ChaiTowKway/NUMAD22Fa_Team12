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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionsList;
import edu.northeastern.numad22fa_team12.outfitTodayModel.Outfit;
import edu.northeastern.numad22fa_team12.outfitTodayModel.SeasonEnum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView minMaxTempTv, avgTempTv, occasionTv, warmthTv;
    private TextView topPre, topNxt, bottomPre, bottomNxt, shoePre, shoeNxt;
    private ImageView warmthImage, topImage, bottomImage, shoeImage;
    private int topIdx, bottomIdx, shoeIdx;
    private String minTemp, maxTemp, avgTemp;
    private FirebaseDatabase database;
    private DatabaseReference userRef, outfitRef;
    private FirebaseAuth userAuth;
    private String userEmailKey = "";
    private String curOccasion;
    private List<String> topUrls, bottomUrls, shoeUrls;
    private String curSeason;
    private String mParam1;
    private String mParam2;

//    List<String> seasonListTop = new ArrayList<>(), seasonListBottom = new ArrayList<>(), seasonListShoe = new ArrayList<>();
//    List<String> occasionListTop = new ArrayList<>(), occasionListBottom = new ArrayList<>(), occasionListShoe = new ArrayList<>();


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
        outfitRef = database.getReference().child("outfit");
        userAuth = FirebaseAuth.getInstance();
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }
        topUrls = new ArrayList<>();
        bottomUrls = new ArrayList<>();
        shoeUrls = new ArrayList<>();

        topIdx = 0;
        bottomIdx = 0;
        shoeIdx = 0;

        curSeason = checkCurSeason();

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

        topPre = (TextView) view.findViewById(R.id.topPrevious);
        topNxt = (TextView) view.findViewById(R.id.topNext);
        bottomPre = (TextView) view.findViewById(R.id.bottomPrevious);
        bottomNxt = (TextView) view.findViewById(R.id.bottomNext);
        shoePre = (TextView) view.findViewById(R.id.shoePrevious);
        shoeNxt = (TextView) view.findViewById(R.id.shoeNext);

        topPre.setOnClickListener(this);
        topNxt.setOnClickListener(this);
        bottomPre.setOnClickListener(this);
        bottomNxt.setOnClickListener(this);
        shoePre.setOnClickListener(this);
        shoeNxt.setOnClickListener(this);

        getCurrUserInfo();
//        getOutfitList();
//        setTopImage();
//        setBottomImage();
//        setShoeImage();
        setWeatherTextViewImageView();

        return view;
    }

    @Override
    public void onClick(View view) {
        int clickedID = view.getId();
        switch (clickedID) {
            case R.id.topPrevious:
                topIdx -= 1;
                if (topIdx < 0) topIdx = topUrls.size() - 1;
                Log.d(TAG, "topPre clicked! idx: " + topIdx);
                setTopImage();
                break;
            case R.id.topNext:
                topIdx += 1;
                if (topIdx == topUrls.size()) topIdx = 0;
                Log.d(TAG, "topNxt clicked! idx: " + topIdx);
                setTopImage();
                break;
            case R.id.bottomPrevious:
                bottomIdx -= 1;
                if (bottomIdx < 0) bottomIdx = bottomUrls.size() - 1;
                Log.d(TAG, "bottomPre clicked! idx: " + bottomIdx);
                setBottomImage();
                break;
            case R.id.bottomNext:
                bottomIdx += 1;
                if (bottomIdx == bottomUrls.size()) bottomIdx = 0;
                Log.d(TAG, "bottomNxt clicked! idx: " + bottomIdx);
                setBottomImage();
                break;
            case R.id.shoePrevious:
                shoeIdx -= 1;
                if (shoeIdx < 0) shoeIdx = shoeUrls.size() - 1;
                Log.d(TAG, "shoePre clicked! idx: " + shoeIdx);
                setShoeImage();
                break;
            case R.id.shoeNext:
                shoeIdx += 1;
                if (shoeIdx == shoeUrls.size()) shoeIdx = 0;
                Log.d(TAG, "shoeNxt clicked! idx: " + shoeIdx);
                setShoeImage();
                break;
        }
    }

    private void setTopImage() {
        if (topImage != null && !topUrls.isEmpty()) {
            Picasso.get().load(topUrls.get(topIdx)).rotate(90).into(topImage);
        }
    }

    private void setBottomImage() {
        if (bottomImage != null && !bottomUrls.isEmpty()) {
            Picasso.get().load(bottomUrls.get(bottomIdx)).rotate(90).into(bottomImage);
        }
    }

    private void setShoeImage() {
        if (shoeImage != null && !shoeUrls.isEmpty()) {
            Picasso.get().load(shoeUrls.get(shoeIdx)).rotate(90).into(shoeImage);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void setWeatherTextViewImageView() {
        if (getArguments() != null && getArguments().getStringArray("tempData") != null) {
            String[] tempData = getArguments().getStringArray("tempData");
            minTemp = tempData[0];
            maxTemp = tempData[1];
            avgTemp = tempData[2];
            Log.d(TAG, String.format("maxT: %s, minT: %s, avgT: %s", maxTemp, minTemp, avgTemp));
            if (minMaxTempTv != null && avgTempTv != null) {
                minMaxTempTv.setText(String.format("Low: %s°F / High: %s°F", minTemp, maxTemp));
                avgTempTv.setText(String.format("Average: %.2f°F", Float.valueOf(avgTemp)));
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
                    if (occasionsList != null) {
                        if (occasionsList.checkOccasion() != "No occasion configured") {
                            curOccasion = occasionsList.checkOccasion();
                            Log.d(TAG, "current occasion: " + curOccasion);
                            setOccasionTextView("Occasion based on your setting: " + curOccasion);
                        } else {
                            Log.d(TAG, "No occasion found!");
                            setOccasionTextView("No occasion found, add occasions for better suggestion!");
                        }
                        getOutfitList();
                    }
                }
            }
        });
    }

//    public Boolean checkSeasonMatch(String itemKey) {
//        final Boolean[] res = {false};
//        outfitRef.child(itemKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Outfit curOutfit = snapshot.getValue(Outfit.class);
//                    if (curOutfit != null && Objects.equals(curSeason.trim(), SeasonEnum.values()[curOutfit.getSeasonId()].toString().trim())) {
//                        res[0] = true;
//                        Log.d(TAG, "current season: " + curSeason);
//                        Log.d(TAG, "outfit season: " + SeasonEnum.values()[curOutfit.getSeasonId()].toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        Log.d(TAG, "compare res: " + res[0].toString());
//        return res[0];
//    }

    public void getOutfitList() {
        topUrls.clear();
        bottomUrls.clear();
        shoeUrls.clear();

        // first check if we have preset occasion
        if (curOccasion != null) {
            // add outfit match both season and occasion
            Log.d(TAG, "current occasion: " + curOccasion);

            // occasion for top
            userRef.child(userEmailKey).child("categoryList").child("tops").child("occasion").child(curOccasion).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();
                            if (!itemUrl.isEmpty()) {
                                String itemKey = Objects.requireNonNull(d.getKey()).toString();
                                outfitRef.child(itemKey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Outfit curOutfit = snapshot.getValue(Outfit.class);
                                            if (curOutfit != null && Objects.equals(curSeason.trim(), SeasonEnum.values()[curOutfit.getSeasonId()].toString().trim())) {
                                                if (!topUrls.contains(itemUrl)) {
                                                    Log.d(TAG, "occasion top URL: " + itemUrl);
                                                    topUrls.add(itemUrl);
                                                }
                                            }
                                        }
                                        setTopImage();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // occasion for bottoms
            userRef.child(userEmailKey).child("categoryList").child("bottoms").child("occasion").child(curOccasion).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();
                            if (!itemUrl.isEmpty()) {
                                String itemKey = Objects.requireNonNull(d.getKey()).toString();
                                outfitRef.child(itemKey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Outfit curOutfit = snapshot.getValue(Outfit.class);
                                            if (curOutfit != null && Objects.equals(curSeason.trim(), SeasonEnum.values()[curOutfit.getSeasonId()].toString().trim())) {
                                                if (!bottomUrls.contains(itemUrl)) {
                                                    Log.d(TAG, "occasion bottom URL: " + itemUrl);
                                                    bottomUrls.add(itemUrl);
                                                }
                                            }
                                        }
                                        setBottomImage();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // occasion for shoes
            userRef.child(userEmailKey).child("categoryList").child("shoes").child("occasion").child(curOccasion).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();

                            if (!itemUrl.isEmpty()) {
                                String itemKey = Objects.requireNonNull(d.getKey()).toString();

                                outfitRef.child(itemKey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Outfit curOutfit = snapshot.getValue(Outfit.class);
                                            if (curOutfit != null && Objects.equals(curSeason.trim(), SeasonEnum.values()[curOutfit.getSeasonId()].toString().trim())) {
                                                if (!shoeUrls.contains(itemUrl)) {
                                                    Log.d(TAG, "occasion shoe URL: " + itemUrl);
                                                    shoeUrls.add(itemUrl);
                                                }
                                            }
                                        }
                                        setShoeImage();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
//                                if (checkSeasonMatch(itemKey)) {
//                                    if (!shoeUrls.contains(itemUrl)) {
//                                        Log.d(TAG, "occasion shoe URL: " + itemUrl);
//                                        shoeUrls.add(itemUrl);
//                                    }
//                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // only check for season if there is no occasion found
            // season for top
            userRef.child(userEmailKey).child("categoryList").child("tops").child("season").child(curSeason).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();
                            if (!itemUrl.isEmpty()) {
                                Log.d(TAG, "curSeason: " + curSeason);
                                Log.d(TAG, "top season URL: " + itemUrl);
                                topUrls.add(itemUrl);
                            }
                        }
                        Log.d(TAG, "season Top: " + topUrls);
                        setTopImage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // season for bottoms
            userRef.child(userEmailKey).child("categoryList").child("bottoms").child("season").child(curSeason).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();
                            if (!itemUrl.isEmpty()) {
                                Log.d(TAG, "curSeason: " + curSeason);
                                Log.d(TAG, "bottom season URL: " + itemUrl);
                                bottomUrls.add(itemUrl);
                            }
                        }
                        Log.d(TAG, "season bottom: " + bottomUrls);
                        setBottomImage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // season for shoes
            userRef.child(userEmailKey).child("categoryList").child("shoes").child("season").child(curSeason).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            String itemUrl = Objects.requireNonNull(d.getValue()).toString();
                            if (!itemUrl.isEmpty()) {
                                Log.d(TAG, "curSeason: " + curSeason);
                                Log.d(TAG, "shoe season URL: " + itemUrl);
                                shoeUrls.add(itemUrl);
                            }
                        }
                        Log.d(TAG, "season shoe: " + shoeUrls);
                        setShoeImage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

//    private void checkIntersectionAddData() {
//        // find the intersection of two list and if occasion or intersection is empty, use the season list
//        List<String> interListTop = new ArrayList<>(seasonListTop), interListBottom = new ArrayList<>(seasonListBottom), interListShoe = new ArrayList<>(seasonListShoe);
//        interListTop.retainAll(occasionListTop);
//        interListBottom.retainAll(occasionListBottom);
//        interListShoe.retainAll(occasionListShoe);
//        Log.d(TAG, "interTop: " + interListTop);
//        Log.d(TAG, "interBottom: " + interListBottom);
//        Log.d(TAG, "interShoe: " + interListShoe);
//
//        if (interListTop.isEmpty()) {
//            topUrls.addAll(seasonListTop);
//        } else {
//            topUrls.addAll(interListTop);
//        }
//
//        if (interListBottom.isEmpty()) {
//            bottomUrls.addAll(seasonListTop);
//        } else {
//            bottomUrls.addAll(interListTop);
//        }
//
//        if (interListShoe.isEmpty()) {
//            shoeUrls.addAll(seasonListTop);
//        } else {
//            shoeUrls.addAll(interListTop);
//        }
//    }

    private String checkCurSeason() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        return SeasonEnum.values()[(currentMonth - 1) / 3].toString();
    }

}