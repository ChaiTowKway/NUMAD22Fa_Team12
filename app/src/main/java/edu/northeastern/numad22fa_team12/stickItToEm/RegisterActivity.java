package edu.northeastern.numad22fa_team12.stickItToEm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.northeastern.numad22fa_team12.R;
import edu.northeastern.numad22fa_team12.model.StickerHistory;
import edu.northeastern.numad22fa_team12.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "registerActivity";

    private EditText userEmail, userName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth myAuth;
    private Button registerBtn, signinBtn;
    private static final String NAME_REGEX = "[a-zA-Z0-9]+",
            EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", DEFAULT_PW = "1234567";
    private static String FCM_REGISTRATION_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userEmail = findViewById(R.id.emailTx);
        userName = findViewById(R.id.userNameTx);
        registerBtn = findViewById(R.id.registerBtn);
        signinBtn = findViewById(R.id.loginBtn);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            String errorMsg = "Failed to get registration token " + task.getException();
                            Log.e(TAG, errorMsg);
                            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (FCM_REGISTRATION_TOKEN == null) {
                                FCM_REGISTRATION_TOKEN = task.getResult();
                            }
                        }
                        String msg = "Registration Token: " + FCM_REGISTRATION_TOKEN;
                        Log.d(TAG, msg);
                    }
                });

        // get the user entered username and check if it already exist in the firebase
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();

                if(name.length() > 0 && name.matches(NAME_REGEX) && email.matches(EMAIL_REGEX)) {
                    createUser(email, name);
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid username!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString().trim();

                myAuth.signInWithEmailAndPassword(email, DEFAULT_PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sign in successful

                            Intent intent = new Intent(RegisterActivity.this, StickItToEmActivity.class);
                            Bundle b = new Bundle();
                            b.putString("userEmail",email);
                            intent.putExtras(b);
                            startActivity(intent);
                            finish();
                        } else {
                            // something went wrong
                            Toast.makeText(RegisterActivity.this, "Something went wrong, try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed
                        Toast.makeText(RegisterActivity.this, "Error: " + e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(RegisterActivity.this, "Canceled, try again!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void createUser(String email, String name) {
        myRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // if the entered username already exist
                    // make a toast saying this username is invalid
                    Toast.makeText(RegisterActivity.this, "Username already exist!",
                            Toast.LENGTH_LONG).show();
                } else {
                    retrieveRegistrationToken();
                    String msg = "Token: " + FCM_REGISTRATION_TOKEN;
//                    myRef.child(name).child("history").setValue(newUser.getHistory());
                    Toast.makeText(RegisterActivity.this, "User created!",
                            Toast.LENGTH_LONG).show();
                    myAuth.createUserWithEmailAndPassword(email,DEFAULT_PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                // user created
                                Intent intent = new Intent(RegisterActivity.this, StickItToEmActivity.class);
                                Bundle b = new Bundle();
                                b.putString("userEmail",email);
                                intent.putExtras(b);
                                startActivity(intent);
                                finish();
                            } else {
                                // something went wrong
                                Toast.makeText(RegisterActivity.this, "Something went wrong, try again!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // failed
                            Toast.makeText(RegisterActivity.this, "Error: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Toast.makeText(RegisterActivity.this, "Canceled, try again!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        User newUser = new User(email, name, FCM_REGISTRATION_TOKEN);
        myRef.child(myAuth.getUid()).setValue(newUser);
    }

    public void retrieveRegistrationToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    String errorMsg = "Failed to get registration token " + task.getException();
                    Log.e(TAG, errorMsg);
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (FCM_REGISTRATION_TOKEN.length() < 1) {
                        FCM_REGISTRATION_TOKEN = task.getResult();
                    }
                    String msg = "Registration Token: " + FCM_REGISTRATION_TOKEN;
                    Log.d(TAG, msg);
                }

            }
        });
    }

}
