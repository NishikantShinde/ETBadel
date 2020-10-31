package com.mnf.etbadel.ui.additem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.ui.additem.adapter.ViewPagerAdapter;
import com.mnf.etbadel.ui.additem.interfaces.ClickListen;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.isuru.sheriff.enums.SheriffPermission;
import info.isuru.sheriff.helper.Sheriff;
import info.isuru.sheriff.interfaces.PermissionListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment implements ClickListen, PermissionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.sw_free)
    Switch swFree;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String[] neighbor = {"neighbor", "neighbor1", "neighbor2", "neighbor3"};
    String[] city = {"city", "city1", "city2", "city3"};
    String[] categories = {};
    Spinner spinnerNeighbor;
    Spinner spinnerCity;
    Spinner spinnerCategory;
    RelativeLayout dropdownArrowNeighbor;
    RelativeLayout dropdownArrowCity;
    RelativeLayout dropdownArrowCategory;

    RelativeLayout condition1Layout;
    RelativeLayout condition2Layout;
    RelativeLayout condition3Layout;

    ImageView nxtImgView;
    ImageView prevImgView;
    Controller controller;
    int lang;
    Sheriff sheriffPermission;
    private boolean isGranted = false;
    private int position;
    private SharedPreferences sharedPreferences;
    private int user_id;
    private int selectedCity = 0;
    private int selectedCategory = 0;
    private int selectedArea = 0;
    private String condition="Barely Used";
    private String isFree="false";
    private List<Image> images;

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        ButterKnife.bind(this,view);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager2);

        viewPager2.setAdapter(new ViewPagerAdapter(getContext(), this));
        controller = Controller.getInstance(getContext());
        init();
        categories = getResources().getStringArray(R.array.categoryList);
        city = getResources().getStringArray(R.array.cityList);
        spinnerNeighbor = view.findViewById(R.id.spinner_neighbor);
        dropdownArrowNeighbor = view.findViewById(R.id.dropdown_arrow_neighbor);
        spinnerCity = view.findViewById(R.id.spinner_city);
        dropdownArrowCity = view.findViewById(R.id.dropdown_arrow_city);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        dropdownArrowCategory = view.findViewById(R.id.dropdown_arrow_category);
        condition1Layout = view.findViewById(R.id.condition1_layout);
        condition2Layout = view.findViewById(R.id.condition2_layout);
        condition3Layout = view.findViewById(R.id.condition3_layout);
        prevImgView = view.findViewById(R.id.prev_img_view);
        nxtImgView = view.findViewById(R.id.nxt_img_view);

//        prevImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager2.setCurrentItem();
//            }
//        });

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, neighbor);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNeighbor.setAdapter(aa);

        ArrayAdapter aa1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, city);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(aa1);

        ArrayAdapter aa2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(aa2);

        dropdownArrowNeighbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerNeighbor.performClick();
            }
        });
        dropdownArrowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCity.performClick();
            }
        });
        dropdownArrowCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCategory.performClick();
            }
        });

        condition1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition=getContext().getResources().getString(R.string.string_condition1);
            }
        });

        condition2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition=getContext().getResources().getString(R.string.string_condition2);
            }
        });

        condition3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition3Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_selected));
                condition1Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition2Layout.setBackground(getResources().getDrawable(R.drawable.rounded_view_condition_notselected));
                condition=getContext().getResources().getString(R.string.string_condition3);
            }
        });
        swFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isFree="true";
                }else {
                    isFree="false";
                }
            }
        });
        return view.getRootView();
    }

    private void init() {
        lang = 0;
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        images= new ArrayList<>();
        controller.getCategoriesDropdown(lang, new CategoriesCallback());
        controller.getCitiesDropdown(lang, new CitiesCallback());
        controller.getAreasDropdown(lang, new AreasCallback());
        sheriffPermission = Sheriff.Builder()
                .with(getActivity())
                .requestCode(1)
                .setPermissionResultCallback(this)
                .askFor(SheriffPermission.STORAGE)
                .build();
    }

    @Override
    public void onClickListen(int position) {
        if (isGranted) {
            this.position = position;
            ImagePicker.create(getActivity())
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .single() // single mode
                    .showCamera(true) // show camera or not (true by default)
                    .start(); // start image picker activity with request code
        } else {
            sheriffPermission.requestPermissions();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
        isGranted = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {
        isGranted = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            images.add(position,image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class CategoriesCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>() {
                            }.getType());
                        } else {
                            String error = jsonObject.getString("Message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    private class CitiesCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>() {
                            }.getType());
                        } else {
                            String error = jsonObject.getString("Message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    private class AreasCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>() {
                            }.getType());
                        } else {
                            String error = jsonObject.getString("Message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    private void saveItem() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Title", etTitle.getText().toString());
        params.put("Description", etDesc.getText().toString());
        params.put("City_Id", selectedCity + "");
        params.put("Cat_Id", selectedCategory + "");
        params.put("Area_Id", selectedArea + "");
        params.put("User_Id", user_id + "");
        params.put("Location", "");
        params.put("Condition", condition);
        params.put("Is_free", isFree);
        if (images.size()>0){
            for (int i=0;i<images.size();i++){
                try {
                    InputStream imageStream = getContext().getContentResolver().openInputStream(images.get(i).getUri());
                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String imageBase = Base64.encodeToString(b, Base64.DEFAULT);
                    if (i==0){
                        params.put("Imgs1_byte", imageBase);
                    }else if (i==1){
                        params.put("Imgs2_byte", imageBase);
                    }else if (i==2){
                        params.put("Imgs3_byte", imageBase);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        controller.itemSave(params, new SaveItemCallback());
    }

    private class SaveItemCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }
}