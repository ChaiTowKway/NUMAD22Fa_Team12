package edu.northeastern.numad22fa_team12.stickItToEm;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;
import edu.northeastern.numad22fa_team12.model.User;

public class StickerDetailsActivity extends AppCompatActivity {
    private final static String TAG = "StickerDetailsActivity----";
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String curUser;
    private String userName;
    private List<Sticker> stickerSentList;
    private List<Sticker> stickerReceivedList;
    private User user = null;
    private RecyclerView sentRecyclerView, receivedRecyclerView;
    private StickerDetailAdapter sentAdapter, receiveAdapter;
    private LinearLayoutManager sentLM, receiveLM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_sticker_details);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        stickerSentList = new ArrayList<>();
        stickerReceivedList = new ArrayList<>();
        userName = "";

        receivedRecyclerView = findViewById(R.id.stickerReceivedRV);
        sentRecyclerView = findViewById(R.id.stickerSentRV);

        receiveLM = new LinearLayoutManager(this);
        sentLM = new LinearLayoutManager(this);

        if (auth.getUid() != null) {
            ref = database.getReference("users").child(auth.getUid());
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error getting data", task.getException());
                    } else {
                        Log.d(TAG,"firebase userAuth.getUid()" + String.valueOf(auth.getUid()));
                        User user = task.getResult().getValue(User.class);
                        if (user != null) {
                            Log.d(TAG, "onComplete: user name" + user.getUserName());
                            userName = user.getUserName();
                        } else {
                            Log.e(TAG, "onComplete: failed to get user");
                        }
                    }
                }
            });

            ref.child("sentStickerList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "sentStickerList Error getting data", task.getException());
                    } else {
                        for (DataSnapshot d : task.getResult().getChildren()) {
                            Sticker stk = d.getValue(Sticker.class);
                            stickerSentList.add(stk);
                            Log.e(TAG, "sentStickerList sticker added: " + stk.toString());
                        }
                    }
                }
            });

            ref.child("receivedStickerList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "receivedStickerList Error getting data", task.getException());
                    } else {
                        for (DataSnapshot d : task.getResult().getChildren()) {
                            Sticker stk = d.getValue(Sticker.class);
                            stickerReceivedList.add(stk);
                            Log.e(TAG, "receivedStickerList sticker added: " + stk.toString());
                        }
                    }
                }
            });

        }
//        createRecyclerView(stickerSentList, stickerReceivedList, userName);
        Log.i(TAG, "send to create rv");


        receiveAdapter = new StickerDetailAdapter(stickerReceivedList, this, userName, false);
        sentAdapter = new StickerDetailAdapter(stickerSentList, this, userName, true);

        receiveAdapter.notifyDataSetChanged();
        sentAdapter.notifyDataSetChanged();

        receivedRecyclerView.setAdapter(receiveAdapter);
        sentRecyclerView.setAdapter(sentAdapter);

        receivedRecyclerView.setLayoutManager(receiveLM);
        sentRecyclerView.setLayoutManager(sentLM);
        Log.i(TAG, "finished create rv");

    }

//    private void createRecyclerView(List<Sticker> sentList, List<Sticker> receivedList, String userName) {
//        RecyclerView sentRecyclerView, receivedRecyclerView;
//        RecyclerView.Adapter sentAdapter, receiveAdapter;
//        RecyclerView.LayoutManager sentLM, receiveLM;
//
//        receivedRecyclerView = findViewById(R.id.stickerReceivedRV);
//        sentRecyclerView = findViewById(R.id.stickerSentRV);
//
//        receiveLM = new LinearLayoutManager(this);
//        sentLM = new LinearLayoutManager(this);
//
//        receiveAdapter = new StickerDetailAdapter(receivedList, this, userName, false);
//        sentAdapter = new StickerDetailAdapter(sentList, this, userName, true);
//
//        receivedRecyclerView.setAdapter(receiveAdapter);
//        sentRecyclerView.setAdapter(sentAdapter);
//
//        receivedRecyclerView.setLayoutManager(receiveLM);
//        sentRecyclerView.setLayoutManager(sentLM);
//        Log.i(TAG, "finished create rv");
//
//    }
}
