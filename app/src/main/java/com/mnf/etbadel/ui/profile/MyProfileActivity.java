package com.mnf.etbadel.ui.profile;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mnf.etbadel.R;
import com.mnf.etbadel.util.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileActivity extends AppCompatActivity {

    @BindView(R.id.title_member)
    TextView titleMember;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_email_id)
    EditText etEmailId;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
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

        if(etEmailId.getText().toString().trim().isEmpty()){
            etEmailId.setError(getResources().getString(R.string.empty_emailid_error));
            isValidData=false;
        }else if(!AppConstants.isValidEmail(etEmailId.getText().toString())){
            etEmailId.setError(getResources().getString(R.string.valid_emailid_error));
            isValidData=false;
        }

        return isValidData;
    }
}