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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.dashboard.adapter.DashboardAdapter;
import com.mnf.etbadel.ui.notifications.adapter.NotificationAdapter;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    LinearLayout categoriesMainLayout;
    ArrayList<CategoriesLayoutClass> categoriesLayoutClasses;
    @BindView(R.id.dashboard_recyclerview)
    RecyclerView dashboardRecyclerview;
    private Controller controller;
    private int selectedCategory = 0;
    private String searchKeyword = "";
    ArrayList<ItemModel> itemModelList=new ArrayList<>();
    DashboardAdapter dashboardAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,root);
        categoriesMainLayout = root.findViewById(R.id.categories_main_layout);
        categoriesLayoutClasses = new ArrayList<>();
        dashboardAdapter=new DashboardAdapter(getContext(),itemModelList);
        dashboardRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        dashboardRecyclerview.setAdapter(dashboardAdapter);
        init();
        return root;
    }

    private void init() {
        int lang = 0;
        controller = Controller.getInstance(getContext());
        controller.getCategoriesDropdown(lang, new CategoriesCallback());
        controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
    }

    private void setCategories(List<DropdownModel> dropdownModelList) {
        String[] categories = getResources().getStringArray(R.array.categoryList);
        ArrayList<String> categoriesList = new ArrayList<String>(Arrays.asList(categories));
        categoriesList.add("All");
//        categoriesList[categoriesList.length]="All";
        for (int i = 0; i < dropdownModelList.size(); i++) {
            CategoriesLayoutClass categoriesLayoutClass = new CategoriesLayoutClass(getContext(), dropdownModelList.get(i), i);
            categoriesLayoutClasses.add(categoriesLayoutClass);
            categoriesMainLayout.addView(categoriesLayoutClass);
        }
    }

    public class CategoriesLayoutClass extends LinearLayout {

        TextView categoriesTxtview;
        LinearLayout categoryCellLayout;
        int index;

        public CategoriesLayoutClass(Context context, DropdownModel dropdownModel, int index) {
            super(context);
            this.index = index;
            View.inflate(context, R.layout.categories_cell_layout, this);
            categoriesTxtview = findViewById(R.id.category_txt);
            categoryCellLayout = findViewById(R.id.category_cell_layout);

            categoryCellLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categoriesLayoutClasses.size(); i++) {
                        if (i == index) {
                            categoriesMainLayout.getChildAt(i).findViewById(R.id.category_cell_layout).setBackground(getResources().getDrawable(R.drawable.rounded_view_primary_color));
                        } else {
                            categoriesMainLayout.getChildAt(i).findViewById(R.id.category_cell_layout).setBackground(getResources().getDrawable(R.drawable.rounded_view));
                        }
                    }
                    selectedCategory = dropdownModel.getId();
                }
            });
            categoriesTxtview.setText(dropdownModel.getName());
        }
    }

    private class CategoriesCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>() {
                            }.getType());
                            setCategories(dropdownModelList);
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error, getActivity().getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
        }
    }

    private class ItemsCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            ArrayList<ItemModel> itemModelList = gson.fromJson(model.toString(), new TypeToken<List<ItemModel>>() {
                            }.getType());
                            dashboardAdapter.updateList(itemModelList);
                            dashboardRecyclerview.scrollToPosition(0);
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error, getActivity().getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
        }
    }
}