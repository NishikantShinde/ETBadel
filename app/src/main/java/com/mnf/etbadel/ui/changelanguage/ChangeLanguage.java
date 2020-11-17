package com.mnf.etbadel.ui.changelanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.util.AppConstants;

import java.util.Locale;

public class ChangeLanguage extends AppCompatActivity {

    private String mLanguageCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        SharedPreferences sharedPreferences= getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        mLanguageCode= sharedPreferences.getString(AppConstants.LANG,"en");
        setAppLocale(mLanguageCode);
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
        recreate();
        Intent intent= new Intent(ChangeLanguage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}