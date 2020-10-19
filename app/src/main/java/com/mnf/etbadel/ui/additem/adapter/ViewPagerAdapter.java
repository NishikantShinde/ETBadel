package com.mnf.etbadel.ui.additem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.mnf.etbadel.R;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

//    private List<String> mData;
    private LayoutInflater mInflater;
//    private ViewPager2 viewPager2;

    public ViewPagerAdapter(Context context/*, List<String> data, ViewPager2 viewPager2*/) {
        this.mInflater = LayoutInflater.from(context);
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
//        String animal = mData.get(position);
//        holder.myTextView.setText(animal);
//        holder.relativeLayout.setBackgroundResource(colorArray[position]);
    }

    @Override
    public int getItemCount() {
//        return mData.size();
        return 3;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView myTextView;
//        RelativeLayout relativeLayout;
//        Button button;

        ViewHolder(View itemView) {
            super(itemView);
//            myTextView = itemView.findViewById(R.id.tvTitle);
//            relativeLayout = itemView.findViewById(R.id.container);
//            button = itemView.findViewById(R.id.btnToggle);
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    if(viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL)
//                        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//                    else{
//                        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//                    }
//                }
//            });
        }
    }

}


