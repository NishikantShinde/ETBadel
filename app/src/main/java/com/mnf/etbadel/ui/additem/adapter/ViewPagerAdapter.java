package com.mnf.etbadel.ui.additem.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnf.etbadel.R;
import com.mnf.etbadel.ui.additem.interfaces.ClickListen;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

//    private List<String> mData;
    private LayoutInflater mInflater;
    private ClickListen clickListen;
    private List<Image> images;
//    private ViewPager2 viewPager2;
    Context context;
    int mPosition;

    public ViewPagerAdapter(Context context, List<Image> images /* ViewPager2 viewPager2*/, ClickListen clickListen) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.clickListen= clickListen;
        this.images=images;
//        this.mData = data;
//        this.viewPager2 = viewPager2;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListen.onClickListen(position);
            }
        });

        holder.adapterImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListen.onClickListen(position);
            }
        });
        if(images!=null && images.size()>0){
            InputStream imageStream = null;
            if(position==mPosition) {
                try {
                    imageStream = context.getContentResolver().openInputStream(images.get(mPosition).getUri());
                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    holder.adapterImageview.setImageBitmap(bm);
                    holder.myTextView.setVisibility(View.GONE);
                    holder.floatingActionButton.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
//        String animal = mData.get(position);
//        holder.myTextView.setText(animal);
//        holder.relativeLayout.setBackgroundResource(colorArray[position]);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setImageView(int position, Image image) {
        mPosition=position;
        images.add(position,image);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
//        RelativeLayout relativeLayout;
        ImageView adapterImageview;
        FloatingActionButton floatingActionButton;
        ViewHolder(View itemView) {
            super(itemView);
            floatingActionButton= itemView.findViewById(R.id.floating_button);
            adapterImageview= itemView.findViewById(R.id.adapter_image_view);
            myTextView= itemView.findViewById(R.id.adapter_txtview);
        }
    }

}


