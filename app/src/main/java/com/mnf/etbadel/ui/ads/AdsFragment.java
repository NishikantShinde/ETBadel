package com.mnf.etbadel.ui.ads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.ui.ads.adapter.AdsAdapter;

public class AdsFragment extends Fragment {

    RecyclerView recyclerViewNotification;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerViewNotification=root.findViewById(R.id.notification_recyclerview);
        AdsAdapter adsAdapter =new AdsAdapter(getContext(),null);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotification.setAdapter(adsAdapter);
        return root;
    }
}