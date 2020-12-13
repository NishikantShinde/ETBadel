package com.mnf.etbadel.util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.mnf.etbadel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.no_btn)
    Button noBtn;
    @BindView(R.id.yes_btn)
    Button yesBtn;
    @BindView(R.id.desc)
    TextView desc;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    boolean show;
    public TradeFragment(boolean show) {
        this.show=show;
        // Required empty public constructor
    }

    CallbackInterface callbackInterface;

    public interface CallbackInterface {
        void doCallbackOnClick();
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
        View view = inflater.inflate(R.layout.fragment_trade, container, false);
        ButterKnife.bind(this,view);
        if (show){
            desc.setVisibility(View.VISIBLE);
        }else {
            desc.setVisibility(View.INVISIBLE);
        }

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callbackInterface.doCallbackOnClick();

            }
        });
        return view.getRootView();
    }

    public void setInterfaceInstance(CallbackInterface callbackInterface){
        this.callbackInterface=callbackInterface;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}