package com.mnf.etbadel.controller;

import android.content.Context;

import com.mnf.etbadel.controller.services.AreaCityService;
import com.mnf.etbadel.controller.services.CategoryService;
import com.mnf.etbadel.controller.services.ItemService;
import com.mnf.etbadel.controller.services.LoginService;
import com.mnf.etbadel.controller.services.ProfileService;
import com.mnf.etbadel.controller.services.RegisterService;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Controller {

    private static Controller instance= new Controller();
    private static Context mContext;
    private Controller() {}

    public static  Controller getInstance(Context context)
    {
        mContext=context;
        return instance;
    }

    public void registerUser(UserModel userModel, Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        RegisterService registerService= retrofit.create(RegisterService.class);
        Call<ResponseBody> userModelCall= registerService.registerUser(userModel);
        userModelCall.enqueue(callback);
    }

    public void login(UserModel userModel, Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        LoginService loginService= retrofit.create(LoginService.class);
        Call<ResponseBody> serviceResponse= loginService.login(userModel);
        serviceResponse.enqueue(callback);
    }

    public void getCategories(Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        CategoryService categoryService= retrofit.create(CategoryService.class);
        Call<ResponseBody> serviceResponse= categoryService.getCategories();
        serviceResponse.enqueue(callback);
    }

    public void getCities(Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        AreaCityService areaCityService= retrofit.create(AreaCityService.class);
        Call<ResponseBody> serviceResponse= areaCityService.getCities();
        serviceResponse.enqueue(callback);
    }

    public void getAreas(Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        AreaCityService areaCityService= retrofit.create(AreaCityService.class);
        Call<ResponseBody> serviceResponse= areaCityService.getAreas();
        serviceResponse.enqueue(callback);
    }

    public void getItems(String cnd, int cat_id,Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        ItemService itemService= retrofit.create(ItemService.class);
        Call<ResponseBody> serviceResponse= itemService.getItems(cnd,cat_id);
        serviceResponse.enqueue(callback);
    }

    public void updateProfile(Map<String,String> params, Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        ProfileService profileService= retrofit.create(ProfileService.class);
        Call<ResponseBody> serviceResponse= profileService.update(params);
        serviceResponse.enqueue(callback);
    }

    public void getProfile(int id, Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        ProfileService profileService= retrofit.create(ProfileService.class);
        Call<ResponseBody> serviceResponse= profileService.getById(id);
        serviceResponse.enqueue(callback);
    }

    public void getCategoriesDropdown(int lang,Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        CategoryService categoryService= retrofit.create(CategoryService.class);
        Call<ResponseBody> serviceResponse= categoryService.getCategoriesDropdown(lang);
        serviceResponse.enqueue(callback);
    }

    public void getCitiesDropdown(int lang,Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        AreaCityService areaCityService= retrofit.create(AreaCityService.class);
        Call<ResponseBody> serviceResponse= areaCityService.getCitiesDropdown(lang);
        serviceResponse.enqueue(callback);
    }

    public void getAreasDropdown(int lang,Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        AreaCityService areaCityService= retrofit.create(AreaCityService.class);
        Call<ResponseBody> serviceResponse= areaCityService.getAreasDropdown(lang);
        serviceResponse.enqueue(callback);
    }

    public void itemSave(Map<String,String> params, Callback<ResponseBody> callback){
        Retrofit retrofit = AppConstants.buildRetrofit(false);
        ItemService itemService= retrofit.create(ItemService.class);
        Call<ResponseBody> serviceResponse= itemService.save(params);
        serviceResponse.enqueue(callback);
    }
}
