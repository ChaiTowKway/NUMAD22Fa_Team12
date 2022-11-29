package edu.northeastern.numad22fa_team12.outfitToday.occasions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.User;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OccasionsList;
import edu.northeastern.numad22fa_team12.outfitTodayModel.UserInfo;
import edu.northeastern.numad22fa_team12.stickItToEm.StickItToEmActivity;
import edu.northeastern.numad22fa_team12.stickItToEm.StickerAdapter;
import edu.northeastern.numad22fa_team12.stickItToEm.UserAdapter;

public class MyOccasions extends AppCompatActivity implements View.OnClickListener, MyOccasionsAdaptor.OnOccasionsListener{
    private static final String TAG = "MyOccasions";
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private String userEmail = "",userEmailKey = "";

    private RecyclerView occasionRecycleView;
    private RecyclerView.Adapter occasionAdapter;
    MyOccasionsAdaptor.OnOccasionsListener onOccasionsListener;
    private RecyclerView.LayoutManager occasionLM;
    private List<String> dateOfWeekList;
    private List<String> occasionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_occasions);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("OutfitTodayUsers");
        userAuth = FirebaseAuth.getInstance();
        if (userAuth.getCurrentUser() != null && userAuth.getCurrentUser().getEmail() != null) {
            userEmail = userAuth.getCurrentUser().getEmail();
            userEmailKey = userAuth.getCurrentUser().getEmail().replace(".", "-");
        }
        createRecycleView();
    }


    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables", "SuspiciousIndentation"})
    private void createRecycleView() {


        occasionRecycleView = findViewById(R.id.recyclerView_occasions);
        dateOfWeekList = new ArrayList<>();
        occasionsList = new ArrayList<>();
        occasionAdapter = new MyOccasionsAdaptor(this, dateOfWeekList, occasionsList, this);
        occasionRecycleView.setAdapter(occasionAdapter);
        occasionLM = new LinearLayoutManager(MyOccasions.this);
        occasionRecycleView.setLayoutManager(occasionLM);

        userRef.child(userEmailKey).child("occasionsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateOfWeekList.clear();
                occasionsList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    if (d != null) {
                        if (d.getValue(String.class).equals("")) {
                            continue;
                        }
                        dateOfWeekList.add(d.getKey());
                        occasionsList.add(d.getValue(String.class));
                    }
                }
                occasionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String deletedDate = dateOfWeekList.get(viewHolder.getAdapterPosition());
                String deletedOccasion = occasionsList.get(viewHolder.getAdapterPosition());

                int position = viewHolder.getAdapterPosition();
                dateOfWeekList.remove(viewHolder.getAdapterPosition());
                occasionsList.remove(viewHolder.getAdapterPosition());
                userRef.child(userEmailKey).child("occasionsList").child(deletedDate).setValue("");
                occasionAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar.make(occasionRecycleView, deletedDate + " occasion deleted!", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateOfWeekList.add(position, deletedDate);
                        occasionsList.add(position, deletedOccasion);
                        userRef.child(userEmailKey).child("occasionsList").child(deletedDate).setValue(deletedOccasion);
                        occasionAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
        }).attachToRecyclerView(occasionRecycleView);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onOccasionClick(int position) {

    }
}