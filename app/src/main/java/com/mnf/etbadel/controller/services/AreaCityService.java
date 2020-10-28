package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AreaCityService {

    @GET(AppConstants.CITIES_ALL)
    Call<ResponseBody> getCities();

    @GET(AppConstants.AREAS_ALL)
    Call<ResponseBody> getAreas();
}
