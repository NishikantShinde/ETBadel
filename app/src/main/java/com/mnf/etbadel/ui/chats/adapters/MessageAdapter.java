package com.mnf.etbadel.ui.chats.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.util.AppConstants;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private ArrayList<MessageModel> messageModels;
    SharedPreferences sharedPreferences;
//    FirebaseUser firebaseUser;
    public MessageAdapter(Context context, ArrayList<MessageModel> messageModels){
        this.context=context;
        this.messageModels=messageModels;
        sharedPreferences= context.getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessageModel messageModel= messageModels.get(position);
        holder.showMessage.setText(messageModel.getMessageTxt());
        if (messageModel.getItemImageUrl1()!=null){
            holder.msgImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(messageModel.getItemImageUrl1()).placeholder(R.drawable.sample).into(holder.msgImage);
        }else {
            holder.msgImage.setVisibility(View.GONE);
        }
        if (messageModel.getDateTime()!=null){
            holder.dateTxt.setText(messageModel.getDateTime());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessage,dateTxt;
        public ImageView msgImage;
        public ViewHolder(View itemView){
            super(itemView);
            showMessage= itemView.findViewById(R.id.show_message);
            msgImage= itemView.findViewById(R.id.msg_image);
            dateTxt= itemView.findViewById(R.id.date_txt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int userId= sharedPreferences.getInt(AppConstants.SF_USER_ID,0);
        if (messageModels.get(position).getSenderId()==userId){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}