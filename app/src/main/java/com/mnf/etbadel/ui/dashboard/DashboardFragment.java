package com.mnf.etbadel.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.dashboard.adapter.DashboardAdapter;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment implements DashboardAdapter.HideShowProgress {

    LinearLayout categoriesMainLayout;
    ArrayList<CategoriesLayoutClass> categoriesLayoutClasses;
    @BindView(R.id.dashboard_recyclerview)
    RecyclerView dashboardRecyclerview;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.no_post)
    LinearLayout noPost;
    private Controller controller;
    private int selectedCategory = 0;
    private String searchKeyword = "";
    boolean isCategoryServicecompleted;
    boolean isItemModelServciecompleted;
    ArrayList<ItemModel> itemsModelList = new ArrayList<>();
    List<DropdownModel> dropdownModelsList = new ArrayList<>();
    DashboardAdapter dashboardAdapter;
    HideShowProgressView hideShowProgressView;

    public DashboardFragment(HideShowProgressView hideShowProgressView) {
        this.hideShowProgressView = hideShowProgressView;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, root);
        categoriesMainLayout = root.findViewById(R.id.categories_main_layout);
        categoriesLayoutClasses = new ArrayList<>();
        dashboardAdapter = new DashboardAdapter((MainActivity) getActivity(), getContext(), itemsModelList, this);
        dashboardRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dashboardRecyclerview.setAdapter(dashboardAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(dashboardRecyclerview);
        init();
        return root;
    }

    private void init() {
        int lang = 0;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        String mLanguageCode=sharedPreferences.getString(AppConstants.LANG,"en");
        assert mLanguageCode != null;
        if (mLanguageCode.equals("ar")){
            lang=1;
        }
        isCategoryServicecompleted = false;
        isItemModelServciecompleted = false;
        controller = Controller.getInstance(getContext());
        hideShowProgressView.showProgress();
        controller.getCategoriesDropdown(lang, new CategoriesCallback());
        //controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSearch.getText().toString().length()==0){
                    searchKeyword="";
                    hideShowProgressView.showProgress();
                    controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Observable<String> obs = RxTextView.textChanges(etSearch)
                .filter(charSequence -> charSequence.length() >= 3)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(charSequence -> charSequence.toString());

        obs.subscribe(string -> {
            fetchData();
        });

    }

    private void fetchData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchKeyword = etSearch.getText().toString().trim();
                hideShowProgressView.showProgress();
                controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
            }
        });

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
        categoriesLayoutClasses.get(0).categoryCellLayout.performClick();
    }

    @Override
    public void showProgress() {
        hideShowProgressView.showProgress();
    }

    @Override
    public void hideProgress() {
        hideShowProgressView.hideProgress();
    }

    public class CategoriesLayoutClass extends LinearLayout {

        TextView categoriesTxtview;
        public LinearLayout categoryCellLayout;
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
                    hideShowProgressView.showProgress();
                    controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
                }
            });
            categoriesTxtview.setText(dropdownModel.getName());
        }
    }

    private class CategoriesCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            isCategoryServicecompleted = true;
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
                            dropdownModelsList = dropdownModelList;
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
            if (isItemModelServciecompleted) {
                hideShowProgressView.hideProgress();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            isCategoryServicecompleted = true;
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
            if (isItemModelServciecompleted) {
                hideShowProgressView.hideProgress();
            }
        }
    }

    private class ItemsCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            isItemModelServciecompleted = true;
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
                            if (itemModelList.size()>0) {
                                dashboardRecyclerview.setVisibility(View.VISIBLE);
                                noPost.setVisibility(View.GONE);
                                itemsModelList = itemModelList;
                                dashboardAdapter.updateList(itemModelList);
                                dashboardRecyclerview.scrollToPosition(0);
                            }else {
                                dashboardRecyclerview.setVisibility(View.GONE);
                                noPost.setVisibility(View.VISIBLE);
                            }
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error, getActivity().getSupportFragmentManager());
                            dashboardRecyclerview.setVisibility(View.GONE);
                            noPost.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            if (isCategoryServicecompleted) {
                hideShowProgressView.hideProgress();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            isItemModelServciecompleted = true;
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
            dashboardRecyclerview.setVisibility(View.GONE);
            noPost.setVisibility(View.VISIBLE);
            if (isCategoryServicecompleted) {
                hideShowProgressView.hideProgress();
            }
        }
    }
}