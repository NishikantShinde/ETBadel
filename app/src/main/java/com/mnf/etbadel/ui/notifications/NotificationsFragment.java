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

public class NotificationsFragment extends Fragment {

    RecyclerView recyclerViewNotification;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private int user_id;
    NotificationAdapter notificationAdapter;
    NavigationInterface navigationInterface;

    public NotificationsFragment(NavigationInterface navigationInterface) {
        this.navigationInterface = navigationInterface;
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
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getInstance(getContext()).getNotificationsById(1, new NotificationCallback());
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
                            notificationAdapter.updateList(notificationModels);
                            if (notificationModels.size()>0)
                                updateServer(notificationModels);
                        } else {
                            String error = jsonObject.getString("Message");
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
            Controller.getInstance(getContext()).readNotification(ids, new UnreadNotificationCallback());
        }
    }

    private class UnreadNotificationCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        boolean modelStr = jsonObject.getBoolean("Success");
                        if (modelStr) {

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
            progressLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            progressLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }
}