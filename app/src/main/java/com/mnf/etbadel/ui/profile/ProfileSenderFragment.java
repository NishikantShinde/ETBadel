package com.mnf.etbadel.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.ui.dashboard.adapter.DashboardAdapter;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;
import com.mnf.etbadel.util.SucessDialogFragment;
import com.mnf.etbadel.util.TradeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileSenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSenderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.sender_name_txt)
    TextView senderNameTxt;
    @BindView(R.id.item_img)
    ImageView itemImg;
    @BindView(R.id.desc_txt)
    TextView descTxt;
    @BindView(R.id.item_name_txt)
    TextView itemNameTxt;
    @BindView(R.id.item_location_txt)
    TextView itemLocationTxt;
    @BindView(R.id.item_area_txt)
    TextView itemAreaTxt;
    @BindView(R.id.item_condition)
    TextView itemCondition;
    @BindView(R.id.btn_trade)
    Button btnTrade;
    @BindView(R.id.item_date)
    TextView itemDate;
    @BindView(R.id.left_arrow)
    ImageView leftArrow;
    @BindView(R.id.right_arrow)
    ImageView rightArrow;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    ArrayList<String> imageList = new ArrayList<>();
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    private int itemId = 2; // HardCoded
    private int itemId;
    private int selectedImg = 0;
    ItemModel itemModel;
    HideShowProgressView hideShowProgressView;
    
    public ProfileSenderFragment(int itemId, HideShowProgressView hideShowProgressView) {
        // Required empty public constructor
        this.itemId=itemId;
        this.hideShowProgressView=hideShowProgressView;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSenderFragment newInstance(String param1, String param2) {
        ProfileSenderFragment fragment = new ProfileSenderFragment(0, null);
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
        View view = inflater.inflate(R.layout.fragment_profile_sender, container, false);
        ButterKnife.bind(this, view);
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.INVISIBLE);
        hideShowProgressView.showProgress();
        Controller.getInstance(getContext()).getItemById(itemId, new GetItemCallback());

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg == 1 && imageList.get(0) != null) {
                    selectedImg = 0;
                    Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(1)).into(img2);
                } else if (selectedImg == 2 && imageList.get(1) != null) {
                    selectedImg = 1;
                    Glide.with(getContext()).load(imageList.get(1)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(2)).into(img3);
                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.size()>2) {
                    if (selectedImg == 0) {
                        selectedImg = 1;
                        Glide.with(getContext()).load(imageList.get(1)).into(itemImg);
                        Glide.with(getContext()).load(imageList.get(0)).into(img2);
                        Glide.with(getContext()).load(imageList.get(2)).into(img3);
                    } else if (selectedImg == 1) {
                        selectedImg = 0;
                        Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                        Glide.with(getContext()).load(imageList.get(1)).into(img2);
                        Glide.with(getContext()).load(imageList.get(2)).into(img3);
                    } else if (selectedImg == 2) {
                        selectedImg = 0;
                        Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                        Glide.with(getContext()).load(imageList.get(1)).into(img2);
                        Glide.with(getContext()).load(imageList.get(2)).into(img3);
                    }
                }else {
                    if (selectedImg == 0) {
                        selectedImg = 1;
                        Glide.with(getContext()).load(imageList.get(1)).into(itemImg);
                        Glide.with(getContext()).load(imageList.get(0)).into(img2);
                    } else if (selectedImg == 1) {
                        selectedImg = 0;
                        Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                        Glide.with(getContext()).load(imageList.get(1)).into(img2);
                    }
                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImg==0){
                    selectedImg=2;
                    Glide.with(getContext()).load(imageList.get(2)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(1)).into(img2);
                    Glide.with(getContext()).load(imageList.get(0)).into(img3);
                }else if (selectedImg==1){
                    selectedImg=0;
                    Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(1)).into(img2);
                    Glide.with(getContext()).load(imageList.get(2)).into(img3);
                }else if (selectedImg==2){
                    selectedImg=0;
                    Glide.with(getContext()).load(imageList.get(0)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(1)).into(img2);
                    Glide.with(getContext()).load(imageList.get(2)).into(img3);
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg==0 && imageList.size()>1){
                    selectedImg=1;
                    Glide.with(getContext()).load(imageList.get(1)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(0)).into(img2);
                }else if (selectedImg==1 && imageList.size()>2){
                    selectedImg=2;
                    Glide.with(getContext()).load(imageList.get(2)).into(itemImg);
                    Glide.with(getContext()).load(imageList.get(1)).into(img3);
                }
            }
        });

        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeFragment.CallbackInterface callbackInterface=new TradeFragment.CallbackInterface() {
                    @Override
                    public void doCallbackOnClick() {
                        SharedPreferences sharedPreferences= getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
                        int senderId= sharedPreferences.getInt(AppConstants.SF_USER_ID,0);
                        NotificationModel notificationModel= new NotificationModel();
                        notificationModel.setUser_Id(itemModel.getUser_Id());
                        notificationModel.setSender_Id(senderId);
                        notificationModel.setItem_Id(itemModel.getId());
                        hideShowProgressView.showProgress();
                        Controller.getInstance(getContext()).saveNotification(notificationModel, new NotificationSaveCallback());
                    }
                };
                TradeFragment alertDialog = new TradeFragment();
                alertDialog.setInterfaceInstance(callbackInterface);
                alertDialog.show(getActivity().getSupportFragmentManager(), "fragment_alert");

            }
        });
        return view.getRootView();
    }

    private class GetItemCallback implements Callback<ResponseBody> {
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
                            itemModel = gson.fromJson(model.toString(), ItemModel.class);
                            if (itemModel.getUser_Name() != null)
                                senderNameTxt.setText(itemModel.getUser_Name());
                            else
                                senderNameTxt.setText("");
                            if (itemModel.getUser_Profile() != null)
                                Glide.with(getContext()).load(itemModel.getUser_Profile()).into(profileImage);
                            if (itemModel.getDescription() != null)
                                descTxt.setText(itemModel.getDescription());
                            else
                                descTxt.setText("");
                            if (itemModel.getTitle() != null)
                                itemNameTxt.setText(itemModel.getTitle());
                            else
                                itemNameTxt.setText("");
                            if (itemModel.getArea_Name() != null)
                                itemAreaTxt.setText(itemModel.getArea_Name());
                            else
                                itemAreaTxt.setText("");
                            if (itemModel.getCondition() != null)
                                itemCondition.setText(itemModel.getCondition());
                            else
                                itemCondition.setText("");
                            if (itemModel.getLocation() != null)
                                itemLocationTxt.setText(itemModel.getLocation());
                            else
                                itemLocationTxt.setText("");
                            if (itemModel.getC_date() != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = null;
                                String dateString = "";
                                try {
                                    date = dateFormat.parse(itemModel.getC_date());
                                    dateString = dateFormat1.format(date);
                                    itemDate.setText(dateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                itemDate.setText("");
                            }
                            if (itemModel.getImg1_url()!=null){
                                selectedImg=0;
                                imageList.add(itemModel.getImg1_url());
                                Glide.with(getContext()).load(itemModel.getImg1_url()).into(itemImg);
                            }
                            if (itemModel.getImg2_url()!=null){
                                img2.setVisibility(View.VISIBLE);
                                imageList.add(itemModel.getImg2_url());
                                Glide.with(getContext()).load(itemModel.getImg2_url()).into(img2);
                            }else {
                                img2.setVisibility(View.GONE);
                            }
                            if (itemModel.getImg3_url()!=null){
                                img3.setVisibility(View.VISIBLE);
                                imageList.add(itemModel.getImg3_url());
                                Glide.with(getContext()).load(itemModel.getImg3_url()).into(img3);
                            }else {
                                img3.setVisibility(View.GONE);
                            }

                        } else {
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error,getActivity().getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            hideShowProgressView.hideProgress();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgressView.hideProgress();
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }

    private class NotificationSaveCallback implements Callback<ResponseBody> {
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
                            NotificationModel notificationModel = gson.fromJson(model.toString(), NotificationModel.class);
                            SucessDialogFragment sucessDialogFragment=new SucessDialogFragment();
                            sucessDialogFragment.show(getActivity().getSupportFragmentManager(),"");
                            Log.e("status", "success");
                        } else {
                            String error = jsonObject.getString("Message");
                            AppConstants.showErroDIalog(error,getActivity().getSupportFragmentManager());
                            Log.e("status", "error " + error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            hideShowProgressView.hideProgress();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgressView.hideProgress();
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }
}