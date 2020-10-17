package com.mnf.etbadel.ui.profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.util.ReplaceFragmentInterface;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.NotificationHolder> {

    Context context;
    ArrayList<NotificationModel> notificationModels;
    ReplaceFragmentInterface replaceFragmentInterface;

    public ProfileAdapter(ReplaceFragmentInterface replaceFragmentInterface, Context context1, ArrayList<NotificationModel> notificationModels){
        this.context=context;
        this.notificationModels=notificationModels;
        this.replaceFragmentInterface=replaceFragmentInterface;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_cell_layout, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.dashboardCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentInterface.performCLick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        CardView dashboardCardview;
        ImageView itemImg;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            dashboardCardview=itemView.findViewById(R.id.dashboard_main_layout);
            itemImg=itemView.findViewById(R.id.item_img);
        }
    }
}
