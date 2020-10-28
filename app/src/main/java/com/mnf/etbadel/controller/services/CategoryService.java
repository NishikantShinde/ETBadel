package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface CategoryService {

    @GET(AppConstants.CATEGORIES_ALL)
    Call<ResponseBody> getCategories();
}
