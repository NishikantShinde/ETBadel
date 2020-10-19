package com.mnf.etbadel.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mnf.etbadel.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    LinearLayout categoriesMainLayout;
    ArrayList<CategoriesLayoutClass> categoriesLayoutClasses;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        categoriesMainLayout=root.findViewById(R.id.categories_main_layout);
        categoriesLayoutClasses=new ArrayList<>();
        setCategories();
        return root;
    }

    private void setCategories() {
        for(int i=0;i<9;i++){
            CategoriesLayoutClass categoriesLayoutClass=new CategoriesLayoutClass(getContext(),"Categories "+i,i);
            categoriesLayoutClasses.add(categoriesLayoutClass);
            categoriesMainLayout.addView(categoriesLayoutClass);
        }
    }

    public class CategoriesLayoutClass extends LinearLayout{

        TextView categoriesTxtview;
        LinearLayout categoryCellLayout;
        int index;
        public CategoriesLayoutClass(Context context, String categoriessString, int index) {
            super(context);
            this.index=index;
            View.inflate(context,R.layout.categories_cell_layout,this);
            categoriesTxtview=findViewById(R.id.category_txt);
            categoryCellLayout=findViewById(R.id.category_cell_layout);

            categoryCellLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<categoriesLayoutClasses.size();i++){
                        if(i==index){
                            categoriesMainLayout.getChildAt(i).findViewById(R.id.category_cell_layout).setBackground(getResources().getDrawable(R.drawable.rounded_view_primary_color));
                        }else {
                            categoriesMainLayout.getChildAt(i).findViewById(R.id.category_cell_layout).setBackground(getResources().getDrawable(R.drawable.rounded_view));
                        }
                    }
                }
            });
            categoriesTxtview.setText(categoriessString);
        }
    }
}