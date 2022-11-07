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
    private String imageSelected, userSelected;
    private Button send, userInfoBtn;
    private final static String DEFAULT_VAL = "THIS IS A DEFAULT VAL";
    private String userName;
    private String userEmail;
    private List<Integer> usedRecord = new StickerHistory().getUsedRecordList();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
//        userRef = database.getReference("users");
//        stickerRef = database.getReference("stickers");
        userRef = database.getReference().child("users");
        stickerRef = database.getReference().child("stickers");


        stickerSelected = R.drawable.cat;
        stickerList = new ArrayList<>();
        stickerList.add(R.drawable.cat);
        stickerList.add(R.drawable.dog);
        stickerList.add(R.drawable.duck);
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(userSelected, DEFAULT_VAL) && !Objects.equals(imageSelected, DEFAULT_VAL)) {
                    userRef.child(Objects.requireNonNull(userAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer senderSent;
                            senderSent = Integer.valueOf(snapshot.child("numberOfStickersSent").getValue().toString());
                            senderSent++;
                            userRef.child(userAuth.getUid()).setValue(senderSent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    
                    userRef.child(userSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer receiverReceived;

//                            curSent = Integer.valueOf(snapshot.child("numberOfStickersSent").getValue().toString());
                            receiverReceived = Integer.valueOf(snapshot.child("numberOfStickersReceived").getValue().toString());
                            receiverReceived++;
                            userRef.child(userSelected).setValue(receiverReceived);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    stickerRef.child(imageSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer totalUsed;
                            totalUsed = Integer.valueOf(snapshot.child("use").getValue().toString());
                            totalUsed++;
                            userRef.child(userAuth.getUid()).setValue(totalUsed);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(StickItToEmActivity.this, "Sticker Sent!",
                            Toast.LENGTH_LONG).show();
                    userAdapter.notifyDataSetChanged();
                    stickerAdapter.notifyDataSetChanged();
                }
            }
        });

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
                    if (d.child("userEmail").getValue() == null
                            || d.child("userName") == null
                            || d.child("userRegistrationToken") == null) {
                        continue;
                    }
                    if (d != null) {
                        User user = d.getValue(User.class);
                        if (user == null) {
                            Log.i(TAG, "user is null!");
                            continue;
                        }
//                        Log.i(TAG, "user add: " + d.child("userName").getValue().toString());
                        Log.i(TAG, "user add: " + user.toString());
//                        userList.add(new User(d.child("userEmail").getValue().toString(), d.child("userName").getValue().toString()));
                        userList.add(new User(user.getUserEmail(), user.getUserName(), user.getUserRegistrationToken()));
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        stickerSelected = R.drawable.cat;
        stickerList = new ArrayList<>();
        stickerList.add(R.drawable.cat);
        stickerList.add(R.drawable.dog);
        stickerList.add(R.drawable.duck);
        stickerList.add(R.drawable.hedgehog);
        stickerList.add(R.drawable.koala);
        stickerList.add(R.drawable.pig);
        stickerList.add(R.drawable.panda);
        stickerList.add(R.drawable.rooster);

        stickerRecyclerView = findViewById(R.id.RecycleView_Stickers);
        stickerLM = new LinearLayoutManager(this);
        stickerRecyclerView.setLayoutManager(stickerLM);
        stickerAdapter = new StickerAdapter(this, stickerList , this,this.usedRecord);


            stickerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot d : snapshot.getChildren()) {
                        if (d != null) {
                            Log.i(TAG, "sticker add: " + d.child("name").getValue().toString());
//                            stickerList.add(new Sticker(d.getValue().toString(), d.child("use").getValue().toString()));
                        }
                    }
                    stickerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
//        }
        DatabaseReference opeatingUserRef = userRef.child(userName).child("history");

        opeatingUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    StickerHistory history = snapshot.getValue(StickerHistory.class);

                    updateUsedRecordList(history);
                    System.out.println(history.getUsedRecordList());
                }
                stickerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        stickerRecyclerView.setAdapter(stickerAdapter);

    }

    public void updateUsedRecordList(StickerHistory h){
        this.usedRecord = h.getUsedRecordList();
    }

    public void sendMessage() {
        Log.i(TAG, "STICK ID" + stickerSelected);
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }

    @Override
    public void onStickerClick(int position) {
        stickerSelected = stickerList.get(position);
        Log.d(TAG, "onStickerClick: stickerSelected " + stickerSelected);
    }

    @Override
    public void onUserClick(int position) {
        userSelected = userList.get(position).getUserName();
        Log.d(TAG, "onStickerClick: userSelected " + userSelected);
    }
}