package com.mnf.etbadel.controller.services;

import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {

    @POST(AppConstants.CHAT_SAVE)
    Call<ResponseBody> saveChat(@Body ChatModel chatModel);
}
