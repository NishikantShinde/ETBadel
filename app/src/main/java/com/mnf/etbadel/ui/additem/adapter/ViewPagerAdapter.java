package com.mnf.etbadel.ui.additem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnf.etbadel.R;
import com.mnf.etbadel.ui.additem.interfaces.ClickListen;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

//    private List<String> mData;
    private LayoutInflater mInflater;
    private ClickListen clickListen;
//    private ViewPager2 viewPager2;

    public ViewPagerAdapter(Context context/*, List<String> data, ViewPager2 viewPager2*/, ClickListen clickListen) {
        this.mInflater = LayoutInflater.from(context);
        this.clickListen= clickListen;
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
        FloatingActionButton floatingActionButton;
        ViewHolder(View itemView) {
            super(itemView);
            floatingActionButton= itemView.findViewById(R.id.floating_button);
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


