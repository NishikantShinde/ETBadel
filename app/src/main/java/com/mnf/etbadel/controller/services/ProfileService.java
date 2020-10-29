package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfileService {

    @FormUrlEncoded
    @POST(AppConstants.PROFILE_UPDATE)
    Call<ResponseBody> update(@FieldMap Map<String, String> params);

    @GET(AppConstants.PROFILE_GETBYID)
    Call<ResponseBody> getById(@Query("Id") int id);
}
