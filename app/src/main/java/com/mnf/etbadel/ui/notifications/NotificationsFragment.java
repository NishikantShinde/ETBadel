package com.mnf.etbadel.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.notifications.adapter.NotificationAdapter;
import com.mnf.etbadel.ui.profile.MyProfileActivity;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    RecyclerView recyclerViewNotification;
    private SharedPreferences sharedPreferences;
    private int user_id;
    NotificationAdapter notificationAdapter;
    NavigationInterface navigationInterface;
    public NotificationsFragment(NavigationInterface navigationInterface) {
        this.navigationInterface=navigationInterface;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        recyclerViewNotification=root.findViewById(R.id.notification_recyclerview);
        ArrayList<NotificationModel> notificationModelArrayList= new ArrayList<>();
        notificationAdapter=new NotificationAdapter(getContext(),notificationModelArrayList,navigationInterface);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotification.setAdapter(notificationAdapter);
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
}