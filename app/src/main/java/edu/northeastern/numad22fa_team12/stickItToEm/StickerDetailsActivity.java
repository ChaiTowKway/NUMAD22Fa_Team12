package edu.northeastern.numad22fa_team12.stickItToEm;

import android.os.Bundle;
import android.util.Log;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_sticker_details);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        stickerSentList = new ArrayList<>();
        stickerReceivedList = new ArrayList<>();

        if (auth.getUid() != null) {
            ref = database.getReference().child(auth.getUid());
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase userAuth.getUid()", String.valueOf(auth.getUid()));
                        User user = task.getResult().getValue(User.class);
                        if (user != null) {
                            Log.d(TAG, "onComplete: user name" + user.getUserName());
                            Log.d(TAG, "Send to rv");
                            stickerReceivedList = user.getReceivedStickerList();
                            stickerSentList = user.getSentStickerList();
                            userName = user.getUserName();
                            createRecyclerView(stickerSentList, stickerReceivedList, userName);
                        } else {
                            Log.e(TAG, "onComplete: failed to get user");
                        }
                    }
                }
            });
        }
//        if (auth.getCurrentUser() != null) {
//            Log.i(TAG, "cur UID: " + auth.getCurrentUser().getUid());
//            curUser = auth.getCurrentUser().getUid();
//            ref = database.getReference().child(curUser);
//            if (ref.child("sentStickerList") != null) {
//                ref.child("sentStickerList").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot d : snapshot.getChildren()) {
//                            if (d != null) {
//                                stickerSentList.add(d.getValue(Sticker.class));
//                                Log.i(TAG, "add sent Sticker: " + d.child("stickerID").getValue());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.i(TAG, error.getDetails());
//                    }
//                });
//            }
//
//            if (ref.child(curUser).child("receivedStickerList") != null) {
//                ref.child(curUser).child("receivedStickerList").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot d : snapshot.getChildren()) {
//                            if (d != null) {
//                                stickerReceivedList.add(d.getValue(Sticker.class));
//                                Log.i(TAG, "add received Sticker: " + d.child("stickerID").getValue());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            if (ref.child(curUser).child("userName") != null) {
//                ref.child(curUser).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        userName = (String) snapshot.getValue();
//                        Log.i(TAG, "username: " + userName);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//            if (userName != null) {
//                Log.i(TAG, "send to create rv");
//                createRecyclerView(stickerSentList, stickerReceivedList, userName);
//            }
//        }
    }

    private void createRecyclerView(List<Sticker> sentList, List<Sticker> receivedList, String userName) {
        RecyclerView sentRecyclerView, receivedRecyclerView;
        RecyclerView.Adapter sentAdapter, receiveAdapter;
        RecyclerView.LayoutManager sentLM, receiveLM;

        receivedRecyclerView = findViewById(R.id.stickerReceivedRV);
        sentRecyclerView = findViewById(R.id.stickerSentRV);

        receiveLM = new LinearLayoutManager(this);
        sentLM = new LinearLayoutManager(this);

        receiveAdapter = new StickerDetailAdapter(receivedList, this, userName, false);
        sentAdapter = new StickerDetailAdapter(sentList, this, userName, true);

        receivedRecyclerView.setAdapter(receiveAdapter);
        sentRecyclerView.setAdapter(sentAdapter);

        receivedRecyclerView.setLayoutManager(receiveLM);
        sentRecyclerView.setLayoutManager(sentLM);
        Log.i(TAG, "create rv");

    }
}
