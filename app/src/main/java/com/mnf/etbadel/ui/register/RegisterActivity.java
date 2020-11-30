package com.mnf.etbadel.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.login.LoginActivity;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    CallbackManager callbackmanager;
    @BindView(R.id.title_member)
    TextView titleMember;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.acceptance1)
    TextView acceptance1;
    @BindView(R.id.acceptance2)
    TextView acceptance2;
    @BindView(R.id.tandc)
    TextView tandc;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.fb_register)
    LinearLayout fbRegister;
    @BindView(R.id.google_register)
    LinearLayout googleRegister;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    private int RC_SIGN_IN = 200;
    private UserModel userModel;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        auth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData())
                    register();
            }
        });
        fbRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fblogin();
            }
        });
        googleRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

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
            userModel = new UserModel();
            userModel.setF_Name(account.getDisplayName());
            userModel.setEmail(account.getEmail());
            userModel.setIs_Gmail_Login(true);
            userModel.setGmail_Token(account.getId());
            Controller controller = Controller.getInstance(RegisterActivity.this);
            controller.registerUser(userModel, new RegisterCallBack());
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
                                                String str_email = "";
                                                if (json.has("email")) {
                                                    str_email = json.getString("email");
                                                }
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("name");
                                                progressBar.setVisibility(View.VISIBLE);
                                                progressLayout.setVisibility(View.VISIBLE);
                                                userModel = new UserModel();
                                                userModel.setF_Name(str_firstname);
                                                userModel.setEmail(str_email);
                                                userModel.setIs_FB_Login(true);
                                                userModel.setFB_Token(str_id);
                                                Controller controller = Controller.getInstance(RegisterActivity.this);
                                                controller.registerUser(userModel, new RegisterCallBack());
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

    private boolean isValidData() {
        boolean isValidData = true;

        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError(getResources().getString(R.string.first_name_string_error));
            isValidData = false;
        }

        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError(getResources().getString(R.string.last_name_string_error));
            isValidData = false;
        }

        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getResources().getString(R.string.password_string_error));
            isValidData = false;
        }

        if (etConfirmPassword.getText().toString().trim().isEmpty()) {
            etConfirmPassword.setError(getResources().getString(R.string.password_string_error));
            isValidData = false;
        } else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
            etConfirmPassword.setError(getResources().getString(R.string.same_password_string_error));
            isValidData = false;
        }

        if (etUserName.getText().toString().trim().isEmpty()) {
            etUserName.setError(getResources().getString(R.string.empty_emailid_error));
            isValidData = false;
        } else if (!AppConstants.isValidEmail(etUserName.getText().toString())) {
            etUserName.setError(getResources().getString(R.string.valid_emailid_error));
            isValidData = false;
        }

        return isValidData;
    }

    private void register() {
        progressBar.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
        userModel = new UserModel();
        userModel.setF_Name(etFirstName.getText().toString());
        userModel.setL_Name(etLastName.getText().toString());
        userModel.setEmail(etUserName.getText().toString());
        userModel.setPassword(etPassword.getText().toString());
        Controller controller = Controller.getInstance(this);
        controller.registerUser(userModel, new RegisterCallBack());
    }

    private class RegisterCallBack implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    Log.e("status", "success");
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            UserModel userModel = gson.fromJson(model.toString(), UserModel.class);
                            Log.e("status", "success");
                            register(userModel.getEmail(), AppConstants.FIREBASE_PASSWORD);
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

    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}