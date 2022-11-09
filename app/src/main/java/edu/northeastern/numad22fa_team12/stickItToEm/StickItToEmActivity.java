package edu.northeastern.numad22fa_team12.stickItToEm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private HashSet<String> userListSet;
    private HashMap<Integer, Integer> stickerListMap;
    private List<Integer> stickerList;
    private List<Integer> usedRecord;
    private int stickerSelected;
    private String imageSelected = "", userSelectedUID = "", userSelected = "", currUser = "";
    private Button send, userInfoBtn;
    private final static String DEFAULT_VAL = "THIS IS A DEFAULT VAL";
    private String userName;
    private String userEmail;
    private String userUID;
    private Parcelable stickerLMState, userLMState;
    private Sticker lastAddedSticker;

    private final  int NOTIFICATION_UNIQUE_ID = 7;
    private static int notificationGeneration = 1;
//    private List<Integer> usedRecord = new StickerHistory().getUsedRecordList();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

        createNotificationChannel();

        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance();
        userRef = database.getReference().child("users");
        stickerRef = database.getReference().child("stickers");
        userUID = userAuth.getUid();
        getCurrUserInfo();

        stickerListMap = new HashMap<>();
        stickerListMap.put(R.drawable.cat, 0);
        stickerListMap.put(R.drawable.dog, 0);
        stickerListMap.put(R.drawable.duck, 0);
        stickerListMap.put(R.drawable.hedgehog, 0);
        stickerListMap.put(R.drawable.koala, 0);
        stickerListMap.put(R.drawable.pig, 0);
        stickerListMap.put(R.drawable.panda, 0);
        stickerListMap.put(R.drawable.rooster, 0);

        createRecycleView();
        getNotificationInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void getNotificationInfo() {
        if (userRef.child(userUID).child("receivedStickerList") != null)
        userRef.child(userUID).child("receivedStickerList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Sticker addedSticker = snapshot.getValue(Sticker.class);
                if (addedSticker != null && addedSticker != lastAddedSticker) {
                    Log.i(TAG, "sticker received " + addedSticker.getStickerID());
                    String sendByUser = addedSticker.getSentByUser();
                    int stickerId = addedSticker.getStickerID();
                    lastAddedSticker = addedSticker;
                    sendNotification(stickerId, sendByUser);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables", "SuspiciousIndentation"})
    private void createRecycleView() {

        userRecyclerView = findViewById(R.id.friendsListRecycleView);
        userList = new ArrayList<>();
        userListSet = new HashSet<>();
        userAdapter = new UserAdapter(this, userList, this);
        userRecyclerView.setAdapter(userAdapter);
        userLM = new LinearLayoutManager(StickItToEmActivity.this);
        userRecyclerView.setLayoutManager(userLM);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    if (d != null) {
                        User user = d.getValue(User.class);
                        if (user == null) {
                            Log.i(TAG, "user is null!");
                            continue;
                        }
                        if (userListSet.contains(user.getUserUID())) {
                            Log.i(TAG, user.getUserName() + " existed!");
                            continue;
                        }
                        userListSet.add(user.getUserUID());
                        userList.add(new User(user.getUserEmail(), user.getUserName(), user.getUserRegistrationToken(), user.getUserUID()));
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
        stickerList = new ArrayList<>();
        usedRecord = new ArrayList<>();
        stickerAdapter = new StickerAdapter(this, stickerList , this,this.usedRecord);

        userRef.child(userUID).child("sentHistoryRecord").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stickerList.clear();
                usedRecord.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    if (d != null) {
                        String id = d.getKey();
                        Integer num = Integer.valueOf(d.getValue().toString());
                        if (id == null || num == null) {
                            Log.i(TAG, "no sticker!");
                            continue;
                        }
                        if (stickerListMap.containsKey(Integer.parseInt(id))) {
                            stickerListMap.put(Integer.parseInt(id), num);
                            stickerList.add(Integer.parseInt(id));
                            usedRecord.add(num);
                            Log.i(TAG, "onDataChange: d " + id + " " + num.toString());
                        }
                    }
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
        getCurrUserInfo();
        if ( userSelectedUID.length() < 1 || stickerSelected < 1) {
            Toast.makeText(StickItToEmActivity.this, "Please select user and sticker first!",
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "sendMessage: STICK ID" + stickerSelected);
            Log.d(TAG, "sendMessage: userSelectedUID" + userSelectedUID);
            return;
        }

        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        Sticker sentSticker = new Sticker(stickerSelected, currUser, userSelected, String.valueOf(date));

        userRef.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) {
                    return;
                }
                int numberOfStickersSent = 0;
                if (snapshot.child("numberOfStickersSent").getValue() != null) {
                    numberOfStickersSent = Integer.parseInt(snapshot.child("numberOfStickersSent").getValue().toString());
                }
                numberOfStickersSent++;
                userRef.child(userUID).child("numberOfStickersSent").setValue(numberOfStickersSent);

                int sentHistoryRecord = 0;
                if (snapshot.child("sentHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue() != null) {
                    sentHistoryRecord = Integer.parseInt(snapshot.child("sentHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue().toString());
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
                int numberOfStickersReceived = 0;
                if (snapshot.child("numberOfStickersReceived").getValue() != null) {
                    numberOfStickersReceived = Integer.parseInt(snapshot.child("numberOfStickersReceived").getValue().toString());
                }
                numberOfStickersReceived++;
                userRef.child(userSelectedUID).child("numberOfStickersReceived").setValue(numberOfStickersReceived);

                int receivedHistoryRecord = 0;
                if (snapshot.child("receivedHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue() != null) {
                    receivedHistoryRecord = Integer.valueOf(snapshot.child("receivedHistoryRecord").child(String.valueOf(sentSticker.getStickerID())).getValue().toString());
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
        Log.d(TAG, "onUserClick: userSelected " + userSelectedUID);
    }

    public void getCurrUserInfo() {
        userRef.child(userUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Log.e(TAG, "Error getting data", task.getException());

                }
                else {
                    Log.d("firebase userAuth.getUid()", String.valueOf(userAuth.getUid()));
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "onComplete: user name" + user.getUserName());
                        currUser = user.getUserName();
                        FCM_REGISTRATION_TOKEN = user.getUserRegistrationToken();
                        TextView textViewUserList = findViewById(R.id.textViewUserList);
                        String output = "Hi " + user.getUserName() + "! Select a friend to chat!";
                        textViewUserList.setText(output);
                    } else {
                        Log.e(TAG, "onComplete: failed to get user");
                    }
                }
            }
        });
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Name";
//            String description = getString(R.string.channel_description);
            String description = "Description";
            int importannce = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Sticker_Channel", name, importannce);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(Integer stickerID, String sentByUser){


        Intent intent = new Intent(this, StickItToEmActivity.class);

        PendingIntent readIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_MUTABLE);

        String channelID = "Sticker_Channel";

        Bitmap bm = BitmapFactory.decodeResource(getResources(), stickerID);
        Notification noti = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("New message" + Integer.toString(notificationGeneration++) )
                .setContentText("From  " + sentByUser).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Ticker text")
                .setLargeIcon(bm)

                .addAction(R.drawable.ic_launcher_foreground, "Read", readIntent)
                .setContentIntent(readIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_UNIQUE_ID + notificationGeneration, noti);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        stickerLMState = stickerLM.onSaveInstanceState();
        userLMState = userLM.onSaveInstanceState();
        outState.putParcelable("stickerLMState", stickerLMState);
        outState.putParcelable("userLMState", userLMState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userLMState = savedInstanceState.getParcelable("userLMState");
        stickerLMState = savedInstanceState.getParcelable("stickerLMState");
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
        if (stickerLMState != null) stickerLM.onRestoreInstanceState(stickerLMState);
        if (userLMState != null) userLM.onRestoreInstanceState(userLMState);
    }
}