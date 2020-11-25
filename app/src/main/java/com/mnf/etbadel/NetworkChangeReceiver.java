package com.mnf.etbadel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mnf.etbadel.ui.noconnectivity.CheckInternetActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        int stat=NetworkUtil.getConnectivityStatus(context);
        if (stat==0){
            intent= new Intent(context, CheckInternetActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
//        String status = NetworkUtil.getConnectivityStatusString(context);
//
//        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}