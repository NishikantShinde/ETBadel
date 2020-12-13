package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.model.MyResponse;
import com.mnf.etbadel.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationSendService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAm7pHlY0:APA91bHDnpfPSyE9Far9MI0JOgrYNBOaOFZhPcDIIL8TPm-Pd1YHOSNUTWkT5nVbkD1LMsxk31mhGE1o2pGZaQQcx6NVm7nVPMGMdwZ9CF_YWzPr_X6jEJGA1aBZQROQyrwFtoYUDMjT"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender bosy);
}
