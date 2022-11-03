package edu.northeastern.numad22fa_team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class StickItToEmActivity extends AppCompatActivity {

    private static final String TAG = "StickItToEmActivity";
    private static final String SERVER_KEY = "AAAAn-7LHFg:APA91bFjKqRC26hJV6TSDwVKKmw2frTjAWPBne9-" +
            "SDObmZhYxgZDBNUOvqN4uYIap1flKY-VMT5NhSfpJ2prSPOEJ2L7_ucxU2ybsayIlExHbfcbUVrOMEL56DzbNhy3yTPTkVq8NHUl";
    private static String FCM_REGISTRATION_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_to_em);

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

}