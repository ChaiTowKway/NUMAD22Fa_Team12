package edu.northeastern.numad22fa_team12.stickItToEm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "registerActivity";

    private EditText userName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.userNameTx);
        loginBtn = findViewById(R.id.loginBtn);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        // get the user entered username and check if it already exist in the firebase
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();

                Log.d(TAG, "curname: " + name);
                Log.d(TAG, (name.length()>0) ? ">0" : "<0");
                Log.d(TAG, name.matches("[a-zA-Z0-9]+") ? "match" : "not match");

                if(name.length() > 0 && name.matches("[a-zA-Z0-9]+")) {
                    createUser(name);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid username!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createUser(String name) {
        myRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // if the entered username already exist
                    // make a toast saying this username is invalid
                    Toast.makeText(RegisterActivity.this, "Username already exist!",
                            Toast.LENGTH_LONG).show();
                } else {
                    User newUser = new User(name);
                    myRef.child(name).setValue(newUser);
                    Toast.makeText(RegisterActivity.this, "User created!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
