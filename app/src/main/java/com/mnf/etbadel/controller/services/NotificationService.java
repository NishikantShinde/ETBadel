package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationService {

    @GET(AppConstants.NOTIFICATION_GETBYUSERID)
    Call<ResponseBody> getById(@Query("uid") int id);

    @POST(AppConstants.NOTIFICATION_SAVED)
    Call<ResponseBody> save(@Body NotificationModel notificationModel);
}
