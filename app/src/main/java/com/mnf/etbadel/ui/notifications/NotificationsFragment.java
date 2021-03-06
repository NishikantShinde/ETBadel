package com.mnf.etbadel.ui.notifications;

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
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.notifications.adapter.NotificationAdapter;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    RecyclerView recyclerViewNotification;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_noti_layout)
    LinearLayout noNotifications;
    private SharedPreferences sharedPreferences;
    private int user_id;
    NotificationAdapter notificationAdapter;
    NavigationInterface navigationInterface;
    HideShowProgressView hideShowProgressView;

    public NotificationsFragment(NavigationInterface navigationInterface, HideShowProgressView hideShowProgressView) {
        this.navigationInterface = navigationInterface;
        this.hideShowProgressView=hideShowProgressView;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this,root);
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        recyclerViewNotification = root.findViewById(R.id.notification_recyclerview);
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notificationModelArrayList, navigationInterface);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotification.setAdapter(notificationAdapter);
        hideShowProgressView.showProgress();
        Controller.getInstance(getContext()).getNotificationsById(user_id, new NotificationCallback()); //HardCoded
        return root;
    }

    private class NotificationCallback implements Callback<ResponseBody> {
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
                            ArrayList<NotificationModel> notificationModels = gson.fromJson(model.toString(), new TypeToken<List<NotificationModel>>() {
                            }.getType());
                            if (notificationModels.size()>0) {
                                notificationAdapter.updateList(notificationModels);
                                recyclerViewNotification.setVisibility(View.VISIBLE);
                                noNotifications.setVisibility(View.GONE);
                                Collections.reverse(notificationModels);
                                updateServer(notificationModels);
                            }else {
                                recyclerViewNotification.setVisibility(View.GONE);
                                noNotifications.setVisibility(View.VISIBLE);
                            }
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error,getActivity().getSupportFragmentManager());
                            Log.e("status", "error " + error);
                        }
                  hideShowProgressView.hideProgress();
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
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }

    private void updateServer(ArrayList<NotificationModel> notificationModels) {
        String ids="";
        for (NotificationModel notificationModel: notificationModels){
            if (!notificationModel.isIs_read()){
                ids=ids+notificationModel.getId()+",";
            }
        }
        if (!ids.equals("")) {
            ids = ids.substring(0, ids.length() - 1);
            Log.e("", "");
            hideShowProgressView.showProgress();
            Controller.getInstance(getContext()).readNotification(ids, new UnreadNotificationCallback());
        }
    }

    private class UnreadNotificationCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                hideShowProgressView.hideProgress();
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        boolean modelStr = jsonObject.getBoolean("Success");
                        if (modelStr) {
                            navigationInterface.updateNotificationCount();
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error,getActivity().getSupportFragmentManager());
                            Log.e("status", "error " + error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgressView.hideProgress();
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }
}