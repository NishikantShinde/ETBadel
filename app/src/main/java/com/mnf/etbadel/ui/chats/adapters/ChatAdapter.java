package com.mnf.etbadel.ui.chats.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.chats.MessageActivity;
import com.mnf.etbadel.util.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder> {

    Context context;
    ArrayList<ChatModel> chatModels;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatModels) {
        this.context = context;
        this.chatModels = chatModels;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_cell_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ChatModel chatModel= chatModels.get(position);
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME,Context.MODE_PRIVATE);
        int userId= sharedPreferences.getInt(AppConstants.SF_USER_ID,0);
        if (chatModel.getUser1Id()==userId){
            holder.usernametxt.setText(chatModel.getUser2Name());
            if (chatModel.getUser2Profile()!=null){
                Glide.with(context).load(chatModel.getUser2Profile()).placeholder(R.drawable.sample).into(holder.profileImage);
            }
        }else {
            holder.usernametxt.setText(chatModel.getUser1Name());
            if (chatModel.getUser1Profile()!=null){
                Glide.with(context).load(chatModel.getUser1Profile()).placeholder(R.drawable.sample).into(holder.profileImage);
            }
        }
        holder.messageTxt.setText(chatModel.getLastMessage());
        holder.dateTxt.setText(chatModel.getLastMessageDateTime());
        holder.chatHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, MessageActivity.class);
                intent.putExtra(AppConstants.CHAT_KEY,chatModel.getChatId());
                context.startActivity(intent);
            }
        });
    }

    public void updateList(ArrayList<ChatModel> chatModels) {
        this.chatModels=chatModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.online_dot_img)
        ImageView onlineDotImg;
        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.usernametxt)
        TextView usernametxt;
        @BindView(R.id.message_txt)
        TextView messageTxt;
        @BindView(R.id.head)
        RelativeLayout head;
        @BindView(R.id.date_txt)
        TextView dateTxt;
        @BindView(R.id.separator)
        View separator;
        @BindView(R.id.chat_head_layout)
        RelativeLayout chatHeadLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}