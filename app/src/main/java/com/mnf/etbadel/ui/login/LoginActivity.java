package com.mnf.etbadel.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.register.RegisterActivity;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.google_login)
    LinearLayout googleLogin;
    GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.back_img)
    ImageView backImg;
    private SharedPreferences sharedPreferences;
    private int RC_SIGN_IN = 200;
    FirebaseAuth auth;

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        auth = FirebaseAuth.getInstance();
        revokeAccess();
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.mnf.etbadel",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.e("KeyHash:", key);
//                //Toast.makeText(this, key, Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }


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
                Fblogin();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressLayout.setVisibility(View.VISIBLE);
            UserModel userModel = new UserModel();
            userModel.setIs_Gmail_Login(true);
            userModel.setEmail(account.getEmail());
            userModel.setGmail_Token(account.getId());
            Controller.getInstance(LoginActivity.this).login(userModel, new LoginServiceCall());
        }
    }

    private void Fblogin() {
        callbackmanager = CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("Success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
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
                                                String str_id = json.getString("id");
                                                progressBar.setVisibility(View.VISIBLE);
                                                progressLayout.setVisibility(View.VISIBLE);
                                                UserModel userModel = new UserModel();
                                                userModel.setIs_FB_Login(true);
                                                userModel.setFB_Token(str_id);
                                                if (json.getString("email") != null) {
                                                    userModel.setEmail(json.getString("email"));
                                                }
                                                Controller.getInstance(LoginActivity.this).login(userModel, new LoginServiceCall());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                        AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
                    }
                });
    }

    private class LoginServiceCall implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            UserModel userModel = gson.fromJson(model.toString(), UserModel.class);
                            loginFirebase(userModel, userModel.getEmail(), AppConstants.FIREBASE_PASSWORD);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            progressLayout.setVisibility(View.GONE);
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

    public void loginFirebase(UserModel userModel, String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.e("status", "success");
                    sharedPreferences.edit().putInt(AppConstants.SF_USER_ID, userModel.getId()).apply();
                    setResult(1000);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void showErroDIalog(String error) {
//        FragmentManager fm = getSupportFragmentManager();
//        ErrorAlertDialogDialogFragment alertDialog = ErrorAlertDialogDialogFragment.newInstance(error,"");
//        alertDialog.show(fm, "fragment_alert");
//    }
}