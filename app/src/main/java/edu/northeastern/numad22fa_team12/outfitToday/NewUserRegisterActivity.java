package edu.northeastern.numad22fa_team12.outfitToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.outfitTodayModel.OutfitTodayUser;

public class NewUserRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "NewUserRegisterActivity";
    private static final String NAME_REGEX = "[a-zA-Z0-9]+",
            EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", DEFAULT_PW = "1234567";
    private EditText userEmail, userName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Button signinBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_register);
        userEmail = findViewById(R.id.editText_email);
        userName = findViewById(R.id.editText_username);
        registerBtn = findViewById(R.id.button_createNewAccount);
        signinBtn = findViewById(R.id.button_signIn);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OutfitTodayUsers");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(NewUserRegisterActivity.this, OutfitToday.class);
            Log.e(TAG, user.getEmail());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void createNewAccount() {
        final String name = userName.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();

        if(name.length() < 1 || !name.matches(NAME_REGEX) || !email.matches(EMAIL_REGEX)) {
            Toast.makeText(NewUserRegisterActivity.this, "Invalid email or username provided!",
                    Toast.LENGTH_LONG).show();
        }

        mAuth.createUserWithEmailAndPassword(email, DEFAULT_PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // user created
//                                Log.e(TAG, "user created, email: " + email);
                    String reformatedEmail = email.replace(".", "-");
                    OutfitTodayUser newUser = new OutfitTodayUser();
                    myRef.child(reformatedEmail).setValue(newUser);
                    Toast.makeText(NewUserRegisterActivity.this, "New user created successfully!",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewUserRegisterActivity.this, UpdateProfile.class);
                    Bundle b = new Bundle();
                    b.putString("userEmail",email);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } else {
                    // something went wrong
                    Toast.makeText(NewUserRegisterActivity.this, "Something went wrong, try another email!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // failed
                Toast.makeText(NewUserRegisterActivity.this, "Error: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(NewUserRegisterActivity.this, "Canceled, try again!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signIn() {
        String email = userEmail.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, DEFAULT_PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //sign in successful
                    Intent intent = new Intent(NewUserRegisterActivity.this, OutfitToday.class);
                    Bundle b = new Bundle();
                    b.putString("userEmail",email);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } else {
                    // something went wrong
                    Toast.makeText(NewUserRegisterActivity.this, "Something went wrong, try again!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // failed
                Toast.makeText(NewUserRegisterActivity.this, "Error: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(NewUserRegisterActivity.this, "Canceled, try again!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID) {
            case R.id.button_signIn:
                signIn();
                break;
            case R.id.button_createNewAccount:
                createNewAccount();
                break;
        }
    }

}