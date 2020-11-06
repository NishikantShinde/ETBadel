package com.mnf.etbadel.util;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConstants {

    public static final String SERVER_VAL = "https://appapi.puravtopiwala.com/api/";
    public static final String LOGIN_URL="User/login";
    public static final String REGISTER_URL="User/save";
    public static final String CATEGORIES_ALL="Category/getall";
    public static final String CATEGORIES_DROPDOWN_ALL="Category/dropdownlist";
    public static final String CITIES_ALL="City/getall";
    public static final String CITIES_DROPDOWN_ALL="City/dropdownlist";
    public static final String AREAS_ALL="Area/getall";
    public static final String AREAS_DROPDOWN_ALL="Area/dropdownlist";
    public static final String ITEMS_ALL="Item/getlist";
    public static final String ITEM_SAVE="Item/save";
    public static final String ITEM_BYID="Item/getbyid";
    public static final String PROFILE_UPDATE="User/update";
    public static final String PROFILE_GETBYID="User/getbyid";
    public static final String NOTIFICATION_GETBYUSERID="Notification/getallbyuserid";
    public static final String NOTIFICATION_UNREADBYUSER="Notification/getunreadbyuserid";
    public static final String NOTIFICATION_READ="Notification/readnotification";
    public static final String NOTIFICATION_SAVED="Notification/save";

    public static final String SHAREDPREFERENCES_NAME="EtBadel_SF";
    public static final String SF_USER_ID="user_id_sf";

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final int FRAGMENT_DASHBOARD=1;
    public static final int FRAGMENT_NOTIFICATION=2;
    public static final int FRAGMENT_ADS=3;
    public static final int FRAGMENT_PROFILE=4;
    public static final int FRAGMENT_SERVICE_AGREEMENT =5;
    public static final int FRAGMENT_PRIVACY_AGREEMENT =6;
    public static final int FRAGMENT_LOGOUT=7;
    public static final int FRAGMENT_NOTIFICATION_LIST=8;
    public static final int FRAGMENT_PRODUCTS =9;
    public static final int FRAGMENT_ADD_PRODUCTS =10;
    public static final int SWITCH_TO_ARABIC =11;
    public static final int FRAGMENT_SENDER_PROFILE =12;


    public static final int TIMEOUT=60*1000;


    // Navigation panel
//	public static final int

    public static Retrofit buildRetrofit(boolean defaultRequest)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        if(!defaultRequest) {

            return new Retrofit.Builder()
                    .baseUrl(AppConstants.SERVER_VAL)
                    .client(okHttpClient.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create(CustomGsonBuilder.getInstance().create()))
                    .build();
        }
        else
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            return new Retrofit.Builder()
                    .baseUrl(AppConstants.SERVER_VAL)
                    .client(okHttpClient.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    public static JSONObject getJsonResponseFromRaw(Response<ResponseBody> response)
    {

        try {
            return new JSONObject(getStringResponseFromRaw(response));
        }catch(Exception ex)
        {
            return null;
        }
    }
    public static JSONArray getJsonArrayResponseFromRaw(Response<ResponseBody> response)
    {

        try {
            return new JSONArray(getStringResponseFromRaw(response));
        }catch(Exception ex)
        {
            return null;
        }
    }

    public static String getStringResponseFromRaw(Response<ResponseBody> response)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {

            reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void showErroDIalog(String error, FragmentManager fm) {
        ErrorAlertDialogDialogFragment alertDialog = ErrorAlertDialogDialogFragment.newInstance(error,"");
        alertDialog.show(fm, "fragment_alert");
    }

    public static boolean isValidEmail(String emailId){
        return emailId.matches(EMAIL_PATTERN);
    }
}
