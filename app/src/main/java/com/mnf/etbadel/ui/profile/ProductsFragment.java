package com.mnf.etbadel.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.profile.adapter.ProfileAdapter;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.EqualSpacingItemDecoration;
import com.mnf.etbadel.util.ReplaceFragmentInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView itemViewRecyclerview;
    ProfileAdapter profileAdapter;
    private int profile_id = 1;

    public ProductsFragment(int senderId) {
        // Required empty public constructor
//        profile_id=senderId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment(0);
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        itemViewRecyclerview = view.findViewById(R.id.item_view_recyclerview);
        ReplaceFragmentInterface replaceFragmentInterface = (ReplaceFragmentInterface) getActivity();
        ArrayList<ItemModel> itemModels = new ArrayList<>();
        profileAdapter = new ProfileAdapter(replaceFragmentInterface, getContext(), itemModels);
        itemViewRecyclerview.setHasFixedSize(true);
        LinearLayoutManager layoutManagerFirst = new GridLayoutManager(getContext(), 3);
        //LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        itemViewRecyclerview.setLayoutManager(layoutManagerFirst);
        itemViewRecyclerview.setAdapter(profileAdapter);
        itemViewRecyclerview.scrollToPosition(0);
        itemViewRecyclerview.addItemDecoration(new EqualSpacingItemDecoration(10));

        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getInstance(getContext()).getProfile(profile_id, new GetProfileCallback());
        return view.getRootView();
    }

    private class GetProfileCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            JSONArray items = new JSONArray();
                            if (model.has("Item")) {
                                items = model.getJSONArray("Item");
                            }
                            Gson gson = new Gson();
                            UserModel userModel = gson.fromJson(model.toString(), UserModel.class);
                            ArrayList<ItemModel> itemModelList = gson.fromJson(items.toString(), new TypeToken<List<ItemModel>>() {
                            }.getType());
                            setDataToUI(userModel, itemModelList);
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
            progressLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            progressLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error),getActivity().getSupportFragmentManager());
        }
    }

    private void setDataToUI(UserModel userModel, ArrayList<ItemModel> itemModelList) {
        if (userModel.getProfile_Image_url() != null) {
            Glide.with(getContext()).load(userModel.getProfile_Image_url()).into(profileImage);
        }
        if (userModel.getF_Name() != null && userModel.getL_Name() != null) {
            txtName.setText(userModel.getF_Name() + " " + userModel.getL_Name());
        } else if (userModel.getF_Name() != null) {
            txtName.setText(userModel.getF_Name());
        }
        profileAdapter.updateList(itemModelList);
    }
}