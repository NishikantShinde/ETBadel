package com.mnf.etbadel.ui.chats.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.chats.MessageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder> {

    Context context;
    ArrayList<ItemModel> itemModels;


    public ChatAdapter(Context context, ArrayList<ItemModel> itemModels) {
        this.context = context;
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_cell_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.chatHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, MessageActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void updateList(ArrayList<ItemModel> ItemModels) {

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.online_dot_img)
        ImageView onlineDotImg;
        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.usernametxt)
        TextView usernametxt;
        @BindView(R.id.relativelayout)
        RelativeLayout relativelayout;
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