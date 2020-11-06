package com.mnf.etbadel.ui.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ItemModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.NotificationHolder> {

    Context context;
    ArrayList<ItemModel> itemModels;
    private int selectedImg = 0;


    //    NavigationInterface navigationInterface;
    public DashboardAdapter(Context context, ArrayList<ItemModel> itemModels) {
        this.context = context;
        this.itemModels = itemModels;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_cell_layout, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        ArrayList<String> imageList = new ArrayList<>();
        ItemModel itemModel = itemModels.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        String dateString = "";
        try {
            date = dateFormat.parse(itemModel.getC_date());
            dateString = dateFormat1.format(date);
            holder.dateTxtview.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (itemModel.getImg1_url() != null) {
            selectedImg = 0;
            imageList.add(itemModel.getImg1_url());
            Glide.with(context).load(itemModel.getImg1_url()).into(holder.itemImg);
        }
        if (itemModel.getImg2_url() != null) {
            imageList.add(itemModel.getImg2_url());
            Glide.with(context).load(itemModel.getImg2_url()).into(holder.subImg1);
        }
        if (itemModel.getImg3_url() != null) {
            imageList.add(itemModel.getImg3_url());
            Glide.with(context).load(itemModel.getImg3_url()).into(holder.subImg2);
        }
        holder.prevImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg==1 && imageList.get(0)!=null){
                    selectedImg=0;
                    Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                }else if (selectedImg==2 && imageList.get(1)!=null){
                    selectedImg=1;
                    Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                }
            }
        });

        holder.nxtImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg==0 && imageList.get(1)!=null){
                    selectedImg=1;
                    Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(0)).into(holder.subImg1);
                }else if (selectedImg==1 && imageList.get(2)!=null){
                    selectedImg=2;
                    Glide.with(context).load(imageList.get(2)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg2);
                }
            }
        });
        holder.dateTxtview.setText(itemModel.getC_date());
        holder.areaTxtview.setText(" :" + itemModel.getArea_Name());
        holder.itemNameTxtview.setText(" :" + itemModel.getTitle());
        holder.cndtTxtview.setText(" :" + itemModel.getCondition());
        holder.dscrTxtview.setText(itemModel.getDescription());
        holder.locTxtview.setText(" :" + itemModel.getLocation());
    }

    public void updateList(ArrayList<ItemModel> itemModels) {
        this.itemModels = itemModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.prev_imgview)
        ImageView prevImgview;
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.nxt_imgview)
        ImageView nxtImgview;
        @BindView(R.id.date_txtview)
        TextView dateTxtview;
        @BindView(R.id.dscr_txtview)
        TextView dscrTxtview;
        @BindView(R.id.item_name_txtview)
        TextView itemNameTxtview;
        @BindView(R.id.loc_txtview)
        TextView locTxtview;
        @BindView(R.id.area_txtview)
        TextView areaTxtview;
        @BindView(R.id.cndt_txtview)
        TextView cndtTxtview;
        @BindView(R.id.submit_btn)
        Button submitBtn;
        @BindView(R.id.sub_img_1)
        ImageView subImg1;
        @BindView(R.id.sub_img_2)
        ImageView subImg2;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
