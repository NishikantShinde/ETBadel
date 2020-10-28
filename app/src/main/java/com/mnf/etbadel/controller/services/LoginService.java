package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST(AppConstants.LOGIN_URL)
    Call<ResponseBody> login(@Body UserModel userModel);
}
