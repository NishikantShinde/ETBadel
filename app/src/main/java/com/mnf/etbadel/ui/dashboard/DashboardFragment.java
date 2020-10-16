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

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    @BindView(R.id.categories_main_layout)
    LinearLayout categoriesMainLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, root);

        setCategories();
        return root;
    }

    private void setCategories() {
        for(int i=0;i<9;i++){
            CategoriesLayoutClass categoriesLayoutClass=new CategoriesLayoutClass(getContext(),"Categories "+i);
            categoriesMainLayout.addView(categoriesLayoutClass);
        }
    }

    public class CategoriesLayoutClass extends LinearLayout{

        TextView categoriesTxtview;
        public CategoriesLayoutClass(Context context, String categoriessString) {
            super(context);
            View.inflate(context,R.layout.categories_cell_layout,this);
            categoriesTxtview=findViewById(R.id.category_txt);
            categoriesTxtview.setText(categoriessString);
        }
    }
}