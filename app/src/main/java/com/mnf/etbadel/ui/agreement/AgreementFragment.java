package com.mnf.etbadel.ui.agreement;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.AgreementModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgreementFragment extends Fragment {


    @BindView(R.id.agreement_txt)
    TextView agreementTxt;

    String agreementType;
    AgreementModel agreementModel;
    public AgreementFragment(String agreementType, AgreementModel agreementModel) {
        this.agreementType=agreementType;
        this.agreementModel=agreementModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_agreement, container, false);
        ButterKnife.bind(this,view);
        if (agreementType.equals("p")){
            if (agreementModel.getPrivacy()!=null)
                agreementTxt.setText(Html.fromHtml(agreementModel.getPrivacy()));
        }else {
            if (agreementModel.getTerms()!=null)
                agreementTxt.setText(Html.fromHtml(agreementModel.getTerms()));
        }
        return view;
    }
}