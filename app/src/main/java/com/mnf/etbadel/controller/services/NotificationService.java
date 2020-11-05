package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {

    @GET(AppConstants.NOTIFICATION_GETBYUSERID)
    Call<ResponseBody> getById(@Query("uid") int id);
}
