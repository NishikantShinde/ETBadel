package com.mnf.etbadel.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.dashboard.adapter.DashboardAdapter;
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

public class DashboardFragment extends Fragment implements DashboardAdapter.HideShowProgress {

    LinearLayout categoriesMainLayout;
    ArrayList<CategoriesLayoutClass> categoriesLayoutClasses;
    @BindView(R.id.dashboard_recyclerview)
    RecyclerView dashboardRecyclerview;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Controller controller;
    private int selectedCategory = 0;
    private String searchKeyword = "";
    boolean isCategoryServicecompleted;
    boolean isItemModelServciecompleted;
    ArrayList<ItemModel> itemsModelList = new ArrayList<>();
    List<DropdownModel> dropdownModelsList = new ArrayList<>();
    DashboardAdapter dashboardAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, root);
        categoriesMainLayout = root.findViewById(R.id.categories_main_layout);
        categoriesLayoutClasses = new ArrayList<>();
        dashboardAdapter = new DashboardAdapter((MainActivity) getActivity(), getContext(), itemsModelList,this);
        dashboardRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dashboardRecyclerview.setAdapter(dashboardAdapter);
        init();
        return root;
    }

    private void init() {
        int lang = 0;
        isCategoryServicecompleted=false;
        isItemModelServciecompleted=false;
        controller = Controller.getInstance(getContext());
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
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
            isCategoryServicecompleted=true;
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
                            dropdownModelsList=dropdownModelList;
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
            if(isItemModelServciecompleted){
                progressBar.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            isCategoryServicecompleted=true;
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
            if(isItemModelServciecompleted){
                progressBar.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
            }
        }
    }

    private class ItemsCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            isItemModelServciecompleted=true;
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
                            itemsModelList=itemModelList;
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
            if(isCategoryServicecompleted){
                progressBar.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            isItemModelServciecompleted=true;
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
            if(isCategoryServicecompleted){
                progressBar.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
            }
        }
    }
}