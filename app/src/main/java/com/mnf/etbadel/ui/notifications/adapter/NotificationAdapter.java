package com.mnf.etbadel.ui.notifications.adapter;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    ArrayList<NotificationModel> notificationModels;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels){
        this.context=context;
        this.notificationModels=notificationModels;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_cell_layout, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        if(position%2==0){
            holder.notificationListCellMainLayout.setBackgroundColor(context.getResources().getColor(R.color.notification_snd_row_color));
        }
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        LinearLayout notificationListCellMainLayout;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            notificationListCellMainLayout=itemView.findViewById(R.id.notification_list_cell_main_layout);
        }
    }
}
