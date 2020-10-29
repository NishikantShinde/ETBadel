package com.mnf.etbadel.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.login.LoginActivity;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

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

    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidData())
                register();
            }
        });

    }

    private boolean isValidData() {
        boolean isValidData=true;

        if(etFirstName.getText().toString().trim().isEmpty()){
            etFirstName.setError(getResources().getString(R.string.first_name_string_error));
            isValidData=false;
        }

        if(etLastName.getText().toString().trim().isEmpty()){
            etLastName.setError(getResources().getString(R.string.last_name_string_error));
            isValidData=false;
        }

        if(etPassword.getText().toString().trim().isEmpty()){
            etPassword.setError(getResources().getString(R.string.password_string_error));
            isValidData=false;
        }

        if(etConfirmPassword.getText().toString().trim().isEmpty()){
            etConfirmPassword.setError(getResources().getString(R.string.password_string_error));
            isValidData=false;
        }else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
            etConfirmPassword.setError(getResources().getString(R.string.same_password_string_error));
            isValidData=false;
        }

        if(etUserName.getText().toString().trim().isEmpty()){
            etUserName.setError(getResources().getString(R.string.empty_emailid_error));
            isValidData=false;
        }else if(!AppConstants.isValidEmail(etUserName.getText().toString())){
            etUserName.setError(getResources().getString(R.string.valid_emailid_error));
            isValidData=false;
        }

        return isValidData;
    }

    private void register(){
        userModel= new UserModel();
        userModel.setF_Name(etFirstName.getText().toString());
        userModel.setL_Name(etLastName.getText().toString());
        userModel.setEmail(etUserName.getText().toString());
        userModel.setPassword(etPassword.getText().toString());
        Controller controller= Controller.getInstance(this);
        controller.registerUser(userModel, new RegisterCallBack());
    }

    private class RegisterCallBack implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()){
                if (response.body()!=null){
                    Log.e("status","success");
                    try {
                        JSONObject jsonObject= AppConstants.getJsonResponseFromRaw(response);
                        String modelStr=jsonObject.getString("Model");
                        if (!modelStr.equals("null")){
                            JSONObject model= jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            UserModel userModel= gson.fromJson(model.toString(), UserModel.class);
                            Log.e("status","success");
                        }else {
                            String error= jsonObject.getString("Message");
                            Log.e("status","error "+error);
                            AppConstants.showErroDIalog(error,getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status","failed");
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getSupportFragmentManager());
        }
    }
}