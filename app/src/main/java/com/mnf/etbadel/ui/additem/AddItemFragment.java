package com.mnf.etbadel.ui.additem;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mnf.etbadel.R;
import com.mnf.etbadel.ui.additem.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String[] neighbor = {"neighbor","neighbor1", "neighbor2", "neighbor3"};
    String[] city = {"city","city1", "city2", "city3"};
    String[] categories = {};
    Spinner spinnerNeighbor;
    Spinner spinnerCity;
    Spinner spinnerCategory;
    RelativeLayout dropdownArrowNeighbor;
    RelativeLayout dropdownArrowCity;
    RelativeLayout dropdownArrowCategory;

    RelativeLayout condition1Layout;
    RelativeLayout condition2Layout;
    RelativeLayout condition3Layout;

    ImageView nxtImgView;
    ImageView prevImgView;
    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_item, container, false);

        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager2);

        viewPager2.setAdapter(new ViewPagerAdapter(getContext()));

        categories=getResources().getStringArray(R.array.categoryList);
        city=getResources().getStringArray(R.array.cityList);
        spinnerNeighbor=view.findViewById(R.id.spinner_neighbor);
        dropdownArrowNeighbor=view.findViewById(R.id.dropdown_arrow_neighbor);
        spinnerCity=view.findViewById(R.id.spinner_city);
        dropdownArrowCity=view.findViewById(R.id.dropdown_arrow_city);
        spinnerCategory=view.findViewById(R.id.spinner_category);
        dropdownArrowCategory=view.findViewById(R.id.dropdown_arrow_category);

        condition1Layout=view.findViewById(R.id.condition1_layout);
        condition2Layout=view.findViewById(R.id.condition2_layout);
        condition3Layout=view.findViewById(R.id.condition3_layout);
        prevImgView=view.findViewById(R.id.prev_img_view);
        nxtImgView=view.findViewById(R.id.nxt_img_view);

//        prevImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager2.setCurrentItem();
//            }
//        });

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, neighbor);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNeighbor.setAdapter(aa);

        ArrayAdapter aa1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, city);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(aa1);

        ArrayAdapter aa2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(aa2);

        dropdownArrowNeighbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerNeighbor.performClick();
            }
        });
        dropdownArrowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCity.performClick();
            }
        });
        dropdownArrowCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCategory.performClick();
            }
        });

        condition1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
            }
        });

        condition2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
            }
        });

        condition3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
            }
        });
        return view.getRootView();
    }
}