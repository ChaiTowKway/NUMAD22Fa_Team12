package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.Sticker;
import edu.northeastern.numad22fa_team12.model.StickerHistory;
import edu.northeastern.numad22fa_team12.model.User;

public class StickItToEmActivity extends AppCompatActivity implements View.OnClickListener, StickerAdapter.OnStickerListener, UserAdapter.OnUserListener{

    private static final String TAG = "StickItToEmActivity";
    private static final String SERVER_KEY = "AAAAn-7LHFg:APA91bFjKqRC26hJV6TSDwVKKmw2frTjAWPBne9-" +
            "SDObmZhYxgZDBNUOvqN4uYIap1flKY-VMT5NhSfpJ2prSPOEJ2L7_ucxU2ybsayIlExHbfcbUVrOMEL56DzbNhy3yTPTkVq8NHUl";
    private static String FCM_REGISTRATION_TOKEN;
    private FirebaseDatabase database;
    private FirebaseAuth userAuth;
    private DatabaseReference userRef, stickerRef;

    private RecyclerView stickerRecyclerView, userRecyclerView;
    private RecyclerView.Adapter stickerAdapter, userAdapter;
    StickerAdapter.OnStickerListener onStickerListener;
    private RecyclerView.LayoutManager stickerLM, userLM;
    private List<User> userList;
    private List<Integer> stickerList;
    private int stickerSelected;
    private String imageSelected, userSelectedUID, userSelected, currUser;
    private Button send, userInfoBtn;
    private final static String DEFAULT_VAL = "THIS IS A DEFAULT VAL";
    private String userName;

    private String userEmail;
    private String userUID;
    private List<Integer> usedRecord = new StickerHistory().getUsedRecordList();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        userRef = database.getReference().child("users");
        stickerRef = database.getReference().child("stickers");
        userUID = userAuth.getUid();
        getCurrUserInfo();

        stickerList = new ArrayList<>();
        stickerList.add(R.drawable.cat);
        stickerList.add(R.drawable.dog);
        stickerList.add(R.drawable.duck);
        stickerList.add(R.drawable.hedgehog);
        stickerList.add(R.drawable.koala);
        stickerList.add(R.drawable.pig);
        stickerList.add(R.drawable.panda);
        stickerList.add(R.drawable.rooster);

        createRecycleView();

        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString("userEmail");
        Query query = userRef.orderByChild("userEmail").equalTo(userEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    User usr = snap.getValue(User.class);
                    userName = usr.getUserName();
                }
                createRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send = findViewById(R.id.sendBtn);
        userInfoBtn = findViewById(R.id.userinfoBtn);

        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(Objects.requireNonNull(userAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String numberOfStickersSent, numberOfStickersReceived;
                        numberOfStickersSent = snapshot.child("numberOfStickersSent").getValue().toString();
                        numberOfStickersReceived = snapshot.child("numberOfStickersReceived").getValue().toString();

                        Toast.makeText(StickItToEmActivity.this,
                                "Total sent: " + numberOfStickersSent +
                                "\nTotal received: " + numberOfStickersReceived,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void retrieveRegistrationToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            String errorMsg = "Failed to get registration token " + task.getException();
                            Log.e(TAG, errorMsg);
                            Toast.makeText(StickItToEmActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        FCM_REGISTRATION_TOKEN = task.getResult();
                        String msg = "Registration Token: " + FCM_REGISTRATION_TOKEN;
                        Log.d(TAG, msg);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser curUser = userAuth.getCurrentUser();
        if (curUser == null) {
            // if user not register, take user to register page
            startActivity(new Intent(StickItToEmActivity.this, RegisterActivity.class));
        }
    }

    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables", "SuspiciousIndentation"})
    private void createRecycleView() {

        userRecyclerView = findViewById(R.id.friendsListRecycleView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList, this);
        userRecyclerView.setAdapter(userAdapter);
        userLM = new LinearLayoutManager(StickItToEmActivity.this);
        userRecyclerView.setLayoutManager(userLM);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
//                    if (d.child("userEmail").getValue() == null
//                            || d.child("userName") == null
//                            || d.child("userRegistrationToken") == null) {
//                        continue;
//                    }
                    if (d != null) {
                        User user = d.getValue(User.class);
                        if (user == null) {
                            Log.i(TAG, "user is null!");
                            continue;
                        } else {
    //                        Log.i(TAG, "user add: " + d.child("userName").getValue().toString());
                            Log.i(TAG, "user add: " + user.toString());
    //                        userList.add(new User(d.child("userEmail").getValue().toString(), d.child("userName").getValue().toString()));
                            userList.add(user);}
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        stickerRecyclerView = findViewById(R.id.RecycleView_Stickers);
        stickerLM = new LinearLayoutManager(this);
        stickerRecyclerView.setLayoutManager(stickerLM);
        stickerAdapter = new StickerAdapter(this, stickerList , this,this.usedRecord);

//            stickerRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for(DataSnapshot d : snapshot.getChildren()) {
//                        if (d != null) {
//                            Log.i(TAG, "sticker add: " + d.child("name").getValue().toString());
////                            stickerList.add(new Sticker(d.getValue().toString(), d.child("use").getValue().toString()));
//                        }
//                    }
//                    stickerAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
//        }
//        DatabaseReference opeatingUserRef = userRef.child(userName).child("history");
//
//        opeatingUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    StickerHistory history = snapshot.getValue(StickerHistory.class);
//
//                    updateUsedRecordList(history);
//                    System.out.println(history.getUsedRecordList());
//                }
//                stickerAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {d
//
//            }
//        });
        stickerRecyclerView.setAdapter(stickerAdapter);

    }

    public void updateUsedRecordList(StickerHistory h){
        this.usedRecord = h.getUsedRecordList();
    }

    public void sendMessage() {
        Log.i(TAG, "STICK ID" + stickerSelected);
        if ( userSelectedUID.length() < 1 || stickerSelected < 1) {
            Toast.makeText(StickItToEmActivity.this, "Please select user and sticker first!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        getCurrUserInfo();

        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        Sticker sentSticker = new Sticker(stickerSelected, currUser, userSelected, String.valueOf(date));

        userRef.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    return;
                }
                Integer numberOfStickersSent;
                numberOfStickersSent = Integer.valueOf(snapshot.child("numberOfStickersSent").getValue().toString());
                if (numberOfStickersSent == null) {
                    numberOfStickersSent = 0;
                }
                numberOfStickersSent++;
                userRef.child(userUID).child("numberOfStickersSent").setValue(numberOfStickersSent);

                Integer sentHistoryRecord;
                sentHistoryRecord = Integer.valueOf(snapshot.child("sentHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue().toString());
                if (sentHistoryRecord == null) {
                    sentHistoryRecord = 0;
                }
                sentHistoryRecord++;
                userRef.child(userUID).child("sentHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).setValue(sentHistoryRecord);
                userRef.child(userUID).child("sentStickerList").push().setValue(sentSticker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userRef.child(userSelectedUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    return;
                }
                Integer numberOfStickersReceived;
                numberOfStickersReceived = Integer.valueOf(snapshot.child("numberOfStickersReceived").getValue().toString());
                if (numberOfStickersReceived == null) {
                    numberOfStickersReceived = 0;
                }
                numberOfStickersReceived++;
                userRef.child(userSelectedUID).child("numberOfStickersReceived").setValue(numberOfStickersReceived);

                Integer receivedHistoryRecord;
                receivedHistoryRecord = Integer.valueOf(snapshot.child("receivedHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue().toString());
                if (receivedHistoryRecord == null) {
                    receivedHistoryRecord = 0;
                }
                receivedHistoryRecord++;
                userRef.child(userSelectedUID).child("receivedHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).setValue(receivedHistoryRecord);
                userRef.child(userSelectedUID).child("receivedStickerList").push().setValue(sentSticker);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

            Toast.makeText(StickItToEmActivity.this, "Sticker Sent!",
                    Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {
        int buttonID = view.getId();
        switch (buttonID) {
            case R.id.sendBtn:
                sendMessage();
                break;
            case R.id.userinfoBtn:
                startActivity(new Intent(StickItToEmActivity.this, StickerDetailsActivity.class));
                break;
        }
    }

    @Override
    public void onStickerClick(int position) {
        stickerSelected = stickerList.get(position);
        Log.d(TAG, "onStickerClick: stickerSelected " + stickerSelected);
    }

    @Override
    public void onUserClick(int position) {
        userSelectedUID = userList.get(position).getUserUID();
        userSelected = userList.get(position).getUserName();
        Log.d(TAG, "onStickerClick: userSelected " + userSelectedUID);
    }

    public void getCurrUserInfo() {
        userRef.child(userUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase ", String.valueOf(task.getResult().getValue()));
                    Log.d("firebase get task", String.valueOf(task.getResult().getValue()));
                    Log.d("firebase userAuth.getUid()", String.valueOf(userAuth.getUid()));
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "onComplete: user name" + user.getUserName());
                        currUser = user.getUserName();
                        FCM_REGISTRATION_TOKEN = user.getUserRegistrationToken();
                    } else {
                        Log.e(TAG, "onComplete: failed to create new user");
                    }
                }
            }
        });
    }
}