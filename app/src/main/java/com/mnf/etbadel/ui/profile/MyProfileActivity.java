package com.mnf.etbadel.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import info.isuru.sheriff.enums.SheriffPermission;
import info.isuru.sheriff.helper.Sheriff;
import info.isuru.sheriff.interfaces.PermissionListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity implements PermissionListener {

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
    Map<String, String> params = new HashMap<String, String>();
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.profile_image_layout)
    LinearLayout profileImageLayout;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profileview)
    ImageView profileview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    private SharedPreferences sharedPreferences;
    private int user_id;
    Sheriff sheriffPermission;
    private boolean isGranted = false;
    private String imageUploadBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        sheriffPermission = Sheriff.Builder()
                .with(MyProfileActivity.this)
                .requestCode(1)
                .setPermissionResultCallback(MyProfileActivity.this)
                .askFor(SheriffPermission.STORAGE)
                .build();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData()) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.VISIBLE);
                    Controller.getInstance(MyProfileActivity.this).updateProfile(params, new ProfileUpdateCallback());
                }
            }
        });
        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGranted) {
                    ImagePicker.create(MyProfileActivity.this)
                            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                            .single() // single mode
                            .showCamera(true) // show camera or not (true by default)
                            .start(); // start image picker activity with request code
                } else {
                    sheriffPermission.requestPermissions();
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGranted) {
                    ImagePicker.create(MyProfileActivity.this)
                            .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                            .single() // single mode
                            .showCamera(true) // show camera or not (true by default)
                            .start(); // start image picker activity with request code
                } else {
                    sheriffPermission.requestPermissions();
                }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
        Controller.getInstance(MyProfileActivity.this).getProfile(user_id, new GetProfileCallback());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only

            Image image = ImagePicker.getFirstImageOrNull(data);
            Glide.with(MyProfileActivity.this).load(image.getUri()).placeholder(R.drawable.ic_camera_alt_24px).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    profileImageLayout.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    profileImageLayout.setVisibility(View.GONE);
                    profileImage.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(profileImage);
            try {
                InputStream imageStream = getContentResolver().openInputStream(image.getUri());
                Bitmap bm = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();
                imageUploadBase = Base64.encodeToString(b, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValidData() {
        boolean isValidData = true;
        params.put("Id", user_id + "");
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError(getResources().getString(R.string.first_name_string_error));
            isValidData = false;
        } else {
            params.put("F_Name", etFirstName.getText().toString());
        }

        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError(getResources().getString(R.string.last_name_string_error));
            isValidData = false;
        } else {
            params.put("L_Name", etLastName.getText().toString());
        }

        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError(getResources().getString(R.string.password_string_error));
            isValidData = false;
        } else {
            params.put("Password", etPassword.getText().toString());
        }

        if (etEmailId.getText().toString().trim().isEmpty()) {
            etEmailId.setError(getResources().getString(R.string.empty_emailid_error));
            isValidData = false;
        } else if (!AppConstants.isValidEmail(etEmailId.getText().toString())) {
            etEmailId.setError(getResources().getString(R.string.valid_emailid_error));
            isValidData = false;
        } else {
            params.put("Email", etEmailId.getText().toString());
        }
        if (imageUploadBase != null) {
            params.put("Profile_Image_byte", imageUploadBase);
        }
        return isValidData;
    }

    @Override
    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
        isGranted = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {
        isGranted = false;
    }

    private class ProfileUpdateCallback implements Callback<ResponseBody> {
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
                            etFirstName.setText(userModel.getF_Name());
                            etLastName.setText(userModel.getL_Name());
                            etEmailId.setText(userModel.getEmail());
                            etPassword.setText(userModel.getPassword());
                            if (userModel.getProfile_Image_url() != null) {
                                Glide.with(MyProfileActivity.this).load(userModel.getProfile_Image_url()).placeholder(R.drawable.ic_camera_alt_24px).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        profileImageLayout.setVisibility(View.VISIBLE);
                                        profileImage.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        profileImageLayout.setVisibility(View.GONE);
                                        profileImage.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                }).into(profileImage);
                            }
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

    private class GetProfileCallback implements Callback<ResponseBody> {
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
                            etFirstName.setText(userModel.getF_Name());
                            etLastName.setText(userModel.getL_Name());
                            etEmailId.setText(userModel.getEmail());
                            etPassword.setText(userModel.getPassword());
                            if (userModel.getProfile_Image_url() != null) {

                                Glide.with(MyProfileActivity.this).load(userModel.getProfile_Image_url()).placeholder(R.drawable.ic_camera_alt_24px).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        profileImageLayout.setVisibility(View.VISIBLE);
                                        profileImage.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        profileImageLayout.setVisibility(View.GONE);
                                        profileImage.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                }).into(profileImage);
                            }
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
}