package edu.northeastern.numad22fa_team12.stickItToEm;

import android.os.Bundle;
import android.os.Parcelable;
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
    private Parcelable sentLMState, receiveLMState;


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
                        Log.d(TAG, "firebase userAuth.getUid()" + String.valueOf(auth.getUid()));
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

            receivedRecyclerView.setLayoutManager(receiveLM);
            sentRecyclerView.setLayoutManager(sentLM);
            receiveAdapter = new StickerDetailAdapter(stickerReceivedList, this, userName, false);
            sentAdapter = new StickerDetailAdapter(stickerSentList, this, userName, true);

            receivedRecyclerView.setAdapter(receiveAdapter);
            sentRecyclerView.setAdapter(sentAdapter);

            Log.i(TAG, "finished create rv");

            ref.child("sentStickerList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    stickerSentList.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        if (d != null) {
                            Sticker stk = d.getValue(Sticker.class);
                            stickerSentList.add(stk);
                            Log.e(TAG, "sentStickerList sticker added: " + stk.toString());
                        }
                    }
                    sentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            ref.child("receivedStickerList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    stickerReceivedList.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        if (d != null) {
                            Sticker stk = d.getValue(Sticker.class);
                            stickerReceivedList.add(stk);
                            Log.e(TAG, "receivedStickerList sticker added: " + stk.toString());
                        }
                    }
                    receiveAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        sentLMState = sentLM.onSaveInstanceState();
        receiveLMState = receiveLM.onSaveInstanceState();
        outState.putParcelable("sent", sentLMState);
        outState.putParcelable("receive", receiveLMState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sentLMState = savedInstanceState.getParcelable("sent");
        receiveLMState = savedInstanceState.getParcelable("receive");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sentLMState != null) sentLM.onRestoreInstanceState(sentLMState);
        if (receiveLMState != null) receiveLM.onRestoreInstanceState(receiveLMState);
    }

}
