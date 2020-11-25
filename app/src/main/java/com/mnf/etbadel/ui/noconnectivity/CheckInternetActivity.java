package com.mnf.etbadel.ui.noconnectivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mnf.etbadel.NetworkUtil;
import com.mnf.etbadel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckInternetActivity extends AppCompatActivity {
    @BindView(R.id.check_again_btn)
    Button checkAgainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet);
        ButterKnife.bind(this);
        checkAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stat= NetworkUtil.getConnectivityStatus(CheckInternetActivity.this);
                if (stat!=0){
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}