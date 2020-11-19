package com.mnf.etbadel.ui.chats.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.ads.adapter.AdsAdapter;
import com.mnf.etbadel.ui.ads.interfaces.AdsActionInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder> {

    Context context; ArrayList<ItemModel> itemModels;
    public ChatAdapter(Context context, ArrayList<ItemModel> itemModels){
        this.context=context;
        this.itemModels =itemModels;
    }

    @NonNull
    @Override
    public ChatAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_cell_layout, parent, false);
        return new ChatAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ItemHolder holder, int position) {

    }
    public void updateList(ArrayList<ItemModel> ItemModels){

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {


        public ItemHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}