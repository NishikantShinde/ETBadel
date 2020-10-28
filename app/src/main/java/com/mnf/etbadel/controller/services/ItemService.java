package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItemService {

    @GET(AppConstants.ITEMS_ALL)
    Call<ResponseBody> getItems(@Query("cnd") String cnd, @Query("cat_id") int cat_id);
}
