package com.mnf.etbadel;

import android.content.IntentFilter;
import android.os.Build;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            NetworkChangeReceiver networkChangeReceiver= new NetworkChangeReceiver();
            IntentFilter intentFilter= new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(networkChangeReceiver,intentFilter);
        }
    }
}
