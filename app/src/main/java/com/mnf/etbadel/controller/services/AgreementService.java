package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AgreementService {

    @GET(AppConstants.AGREEMENT_GET)
    Call<ResponseBody> getAgreement(@Query("lang") int id);
}
