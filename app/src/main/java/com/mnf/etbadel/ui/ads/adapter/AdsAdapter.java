package com.mnf.etbadel.ui.ads.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.ads.interfaces.AdsActionInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ItemHolder> {

    Context context;
    ArrayList<ItemModel> itemModels;
    AdsActionInterface adsActionInterface;
    public AdsAdapter(Context context, ArrayList<ItemModel> itemModels,AdsActionInterface adsActionInterface){
        this.context=context;
        this.itemModels =itemModels;
        this.adsActionInterface=adsActionInterface;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list_cell_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(position%2==0){
            holder.ItemListCellMainLayout.setBackgroundColor(context.getResources().getColor(R.color.notification_snd_row_color));
        }else {
            holder.ItemListCellMainLayout.setBackgroundColor(context.getResources().getColor(R.color.notification_fst_row_color));
        }
        ItemModel itemModel= itemModels.get(position);
        if (itemModel!=null){
            holder.notificationTxt.setText(itemModel.getTitle());
        }
        if (itemModel.getC_date() != null) {
            String dT;
            if (itemModel.getC_date().length()>19) {
                dT = itemModel.getC_date().substring(0, itemModel.getC_date().lastIndexOf("."));
            }else {
                dT=itemModel.getC_date();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            String dateString = "";
            try {
                date = dateFormat.parse(dT);
                dateString = dateFormat1.format(date);
                holder.dateTxt.setText(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.dateTxt.setText("");
        }
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adsActionInterface.deleteAd(position,itemModel);
            }
        });
    }
    public void updateList(ArrayList<ItemModel> ItemModels){
        this.itemModels =ItemModels;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        LinearLayout ItemListCellMainLayout;
        TextView notificationTxt;
        TextView dateTxt;
        LinearLayout editlayout, deleteLayout;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ItemListCellMainLayout=itemView.findViewById(R.id.notification_list_cell_main_layout);
            notificationTxt=itemView.findViewById(R.id.notification_txt);
            dateTxt=itemView.findViewById(R.id.date_txt);
            editlayout=itemView.findViewById(R.id.edit_layout);
            deleteLayout=itemView.findViewById(R.id.delete_layout);
        }
    }
}
