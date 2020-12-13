package com.mnf.etbadel.controller.services;

import com.facebook.internal.AppCall;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface RegisterService {

    @POST(AppConstants.REGISTER_URL)
    Call<ResponseBody> registerUser(@Body UserModel userModel, @Query("lang") int l);
}
