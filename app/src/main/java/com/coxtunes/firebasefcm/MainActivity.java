package com.coxtunes.firebasefcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.coxtunes.firebasefcm.notification.Configuration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We Need to get token HERE
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (task.isSuccessful())
                {
                    TextView textView = findViewById(R.id.tokenid);
                    textView.setText(task.getResult().getToken());

                    Log.d("MYTOKEN",task.getResult().getToken());
                }
            }
        });

        // Here i subcribe to global (For all user not for specific group or other) we can use this method in setting also for control notification
        FirebaseMessaging.getInstance().subscribeToTopic(Configuration.TOPIC_GLOBAL).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Global topic subscription successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Global topic subscription failed", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
