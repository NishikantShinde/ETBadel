package com.mnf.etbadel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mnf.etbadel.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    RoundCornerProgressBar roundCornerProgressBar;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putString(AppConstants.LANG,"ar").apply();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        roundCornerProgressBar = (RoundCornerProgressBar) findViewById(R.id.progressBarSimple4);
        new CountDownTimer(10000, 2000) {

            public void onTick(long millisUntilFinished) {
                progress = progress + 20;
                roundCornerProgressBar.setProgress(progress);
            }

            public void onFinish() {
//                roundCornerProgressBar.setProgress(100);
            }
        }.start();


        roundCornerProgressBar.setOnProgressChangedListener(new BaseRoundCornerProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(View view, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                if (progress >= 100) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId();
                /*.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

            }
        });*/
    }
}