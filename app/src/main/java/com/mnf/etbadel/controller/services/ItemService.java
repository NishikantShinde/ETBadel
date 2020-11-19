package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemService {

    @GET(AppConstants.ITEMS_ALL)
    Call<ResponseBody> getItems(@Query("cnd") String cnd, @Query("cat_id") int cat_id);

    @FormUrlEncoded
    @POST(AppConstants.ITEM_SAVE)
    Call<ResponseBody> save(@FieldMap Map<String, String> params);

    @GET(AppConstants.ITEM_BYID)
    Call<ResponseBody> getItemById(@Query("Id") int id);

    @GET(AppConstants.ITEM_BYUSERID)
    Call<ResponseBody> getItemByUserId(@Query("uId") int id);

    @POST(AppConstants.ITEM_DELETE)
    Call<ResponseBody> delete(@Query("Id") int id);
}
