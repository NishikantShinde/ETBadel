package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {

    @GET(AppConstants.CATEGORIES_ALL)
    Call<ResponseBody> getCategories();

    @GET(AppConstants.CATEGORIES_DROPDOWN_ALL)
    Call<ResponseBody> getCategoriesDropdown(@Query("lang") int lang);
}
