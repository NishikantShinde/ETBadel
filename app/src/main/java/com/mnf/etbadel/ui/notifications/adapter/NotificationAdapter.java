package com.mnf.etbadel.ui.notifications.adapter;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.ui.NavigationInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    ArrayList<NotificationModel> notificationModels;
    NavigationInterface navigationInterface;
    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels, NavigationInterface navigationInterface){
        this.context=context;
        this.notificationModels=notificationModels;
        this.navigationInterface=navigationInterface;
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
        NotificationModel notificationModel= notificationModels.get(position);
        holder.notificationTxt.setText(notificationModel.getSenderName()+" "+context.getResources().getString(R.string.notification_txt_string)+" "+notificationModel.getItemName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        String dateString="";
        try {
            date = dateFormat.parse(notificationModel.getC_date());
            dateString=dateFormat1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.dateTxt.setText(dateString);
        holder.notificationListCellMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationInterface.NavigateFragment(notificationModel.getSender_Id());
            }
        });
    }

    public void updateList(ArrayList<NotificationModel> notificationModels){
        this.notificationModels=notificationModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        LinearLayout notificationListCellMainLayout;
        TextView notificationTxt, dateTxt;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            notificationListCellMainLayout=itemView.findViewById(R.id.notification_list_cell_main_layout);
            notificationTxt= itemView.findViewById(R.id.notification_txt);
            dateTxt= itemView.findViewById(R.id.date_txt);
        }
    }
}
