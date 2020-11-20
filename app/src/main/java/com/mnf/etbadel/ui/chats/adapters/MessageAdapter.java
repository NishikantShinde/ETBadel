package com.mnf.etbadel.ui.chats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
//    private ArrayList<ChatModel> chatModels;
//    FirebaseUser firebaseUser;
    public MessageAdapter(Context context/*, ArrayList<ChatModel> chatModels*/){
        this.context=context;
//        this.chatModels=chatModels;
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
//        ChatModel chatModel= chatModels.get(position);
        holder.showMessage.setText("Hi");
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessage;

        public ViewHolder(View itemView){
            super(itemView);
            showMessage= itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        if (chatModels.get(position).getSender().equals(firebaseUser.getUid())){
//            return MSG_TYPE_RIGHT;
//        }else {
//            return MSG_TYPE_LEFT;
//        }
    }
}