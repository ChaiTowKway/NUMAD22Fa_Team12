package edu.northeastern.numad22fa_team12.outfitToday;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.northeastern.numad22fa_team12.R;

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
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        minMaxTempTv = (TextView) view.findViewById(R.id.minMaxTemp);
        avgTempTv = (TextView) view.findViewById(R.id.avgTemp);

        if (getArguments() != null && getArguments().getStringArray("tempData") != null) {
            String[] tempData = getArguments().getStringArray("tempData");
            minTemp = tempData[0];
            maxTemp = tempData[1];
            avgTemp = tempData[2];
            Log.d(TAG, String.format("maxT: %s, minT: %s, avgT: %s", maxTemp, minTemp, avgTemp));
            updateMinMaxTv(String.format("L: %s°F / H: %s°F", minTemp, maxTemp));
            updateAvgTv(String.format("Today's temperature: %.2f°F", Float.valueOf(avgTemp)));
        }
        return view;
    }

    public void updateMinMaxTv(String s) {
        minMaxTempTv.setText(s);
    }

    public void updateAvgTv(String s) {
        avgTempTv.setText(s);
    }
}