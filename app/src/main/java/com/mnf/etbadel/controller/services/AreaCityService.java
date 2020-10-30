package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AreaCityService {

    @GET(AppConstants.CITIES_ALL)
    Call<ResponseBody> getCities();

    @GET(AppConstants.AREAS_ALL)
    Call<ResponseBody> getAreas();

    @GET(AppConstants.CITIES_DROPDOWN_ALL)
    Call<ResponseBody> getCitiesDropdown(@Query("lang") int lang);

    @GET(AppConstants.AREAS_DROPDOWN_ALL)
    Call<ResponseBody> getAreasDropdown(@Query("lang") int lang);
}
