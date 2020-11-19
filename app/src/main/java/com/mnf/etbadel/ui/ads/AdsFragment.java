package com.mnf.etbadel.ui.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.ads.adapter.AdsAdapter;
import com.mnf.etbadel.ui.ads.interfaces.AdsActionInterface;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsFragment extends Fragment implements AdsActionInterface {

    RecyclerView recyclerView;
    Controller controller;
//    @BindView(R.id.progress_layout)
//    LinearLayout progressLayout;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;
    @BindView(R.id.no_post_layout)
    LinearLayout noPostLayout;
    AdsAdapter adsAdapter;
    private SharedPreferences sharedPreferences;
    private int user_id;
    ArrayList<ItemModel> itemModelList= new ArrayList<>();
    HideShowProgressView hideShowProgressView;

    public AdsFragment(HideShowProgressView hideShowProgressView) {
        this.hideShowProgressView = hideShowProgressView;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ads, container, false);
        ButterKnife.bind(this,root);
        recyclerView = root.findViewById(R.id.notification_recyclerview);
        adsAdapter = new AdsAdapter(getContext(), itemModelList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adsAdapter);
        getData();
        return root;
    }

    private void getData() {
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        controller= Controller.getInstance(getContext());
        noPostLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        hideShowProgressView.showProgress();
        controller.getItemByUserId(user_id, new GetItemByUserCallback());
    }

    @Override
    public void deleteAd(int position, ItemModel itemModel) {
        int index=0; boolean isFound=false;
        for (int i=0;i<itemModelList.size();i++){
            if (itemModel.getId()==itemModelList.get(i).getId()){
                isFound=true;
                index=i;
            }
        }
        hideShowProgressView.showProgress();
        controller.deleteItem(itemModel.getId(), new DeleteItemCallback(isFound,index));

    }

    private class GetItemByUserCallback implements Callback<ResponseBody> {
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
                            itemModelList.clear();
                            itemModelList = gson.fromJson(model.toString(), new TypeToken<List<ItemModel>>() {
                            }.getType());
                            if (itemModelList.size()>0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                noPostLayout.setVisibility(View.GONE);
                                setDataToUI(itemModelList);
                            }else {
                                noPostLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            noPostLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error, getActivity().getSupportFragmentManager());
                            Log.e("status", "error " + error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        noPostLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
            }
            hideShowProgressView.hideProgress();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status", "failed");
            hideShowProgressView.hideProgress();
            noPostLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
        }
    }

    private void setDataToUI(ArrayList<ItemModel> itemModelList) {
        adsAdapter.updateList(itemModelList);
    }

    private class DeleteItemCallback implements Callback<ResponseBody> {
        boolean isFound; int index;
        public DeleteItemCallback(boolean isFound, int index) {
            this.isFound=isFound;
            this.index=index;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        assert jsonObject != null;
                        if (jsonObject.getBoolean("Success")) {
                            if (isFound) {
                                itemModelList.remove(index);
                                adsAdapter.updateList(itemModelList);
                            }
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error, getActivity().getSupportFragmentManager());
                            Log.e("status", "error " + error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            hideShowProgressView.hideProgress();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgressView.hideProgress();
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getActivity().getSupportFragmentManager());
        }
    }
}