package com.mnf.etbadel.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.register.RegisterActivity;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackmanager;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        LinearLayout fbLogin = findViewById(R.id.fb_login);
        sharedPreferences = getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mnf.etbadel",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", key);
                Toast.makeText(this, key, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validData()) {
                    loginWithCredentials();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                Fblogin();
            }
        });
    }

    private boolean validData() {

        boolean isValid = true;

        if (etUserName.getText().toString().trim().isEmpty()) {
            etUserName.setError(getResources().getString(R.string.username_error_string));
            isValid = false;
        }

        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getResources().getString(R.string.password_error_string));
            isValid = false;
        }
        return isValid;
    }

    private void loginWithCredentials() {
        progressBar.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        UserModel userModel = new UserModel();
        userModel.setEmail(userName);
        userModel.setPassword(password);
        Controller.getInstance(this).login(userModel, new LoginServiceCall());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackmanager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Fblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressBar.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        Log.d("TAG_CANCEL", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        progressBar.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        Log.d("TAG_ERROR", error.toString());
                        AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
                    }
                });
    }

    private class LoginServiceCall implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            UserModel userModel = gson.fromJson(model.toString(), UserModel.class);
                            Log.e("status", "success");
                            sharedPreferences.edit().putInt(AppConstants.SF_USER_ID, userModel.getId()).apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status", "failed");
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
        }
    }

//    private void showErroDIalog(String error) {
//        FragmentManager fm = getSupportFragmentManager();
//        ErrorAlertDialogDialogFragment alertDialog = ErrorAlertDialogDialogFragment.newInstance(error,"");
//        alertDialog.show(fm, "fragment_alert");
//    }
}