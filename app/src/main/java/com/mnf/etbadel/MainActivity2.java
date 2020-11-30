package com.mnf.etbadel;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Menu;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.mnf.etbadel.ui.login.LoginActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity2 extends AppCompatActivity {

    RoundCornerProgressBar roundCornerProgressBar;

    int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        roundCornerProgressBar=(RoundCornerProgressBar) findViewById(R.id.progressBarSimple4);
        new CountDownTimer(10000, 2000) {

            public void onTick(long millisUntilFinished) {
                progress=progress+20;
                roundCornerProgressBar.setProgress(progress);
            }

            public void onFinish() {
//                roundCornerProgressBar.setProgress(100);
            }
        }.start();


        roundCornerProgressBar.setOnProgressChangedListener(new BaseRoundCornerProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(View view, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                if(progress>=100){
                    Intent intent=new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}