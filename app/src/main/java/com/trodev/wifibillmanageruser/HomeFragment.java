package com.trodev.wifibillmanageruser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;


public class HomeFragment extends Fragment {

    MaterialCardView packagesMc, monthlyMc, payMc, billHistoryMc, locationMc, supportMc;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*init view*/
        packagesMc = view.findViewById(R.id.packagesMc);
        monthlyMc = view.findViewById(R.id.monthlyMc);
        payMc = view.findViewById(R.id.payMc);
        billHistoryMc = view.findViewById(R.id.bilHistoryMc);
        locationMc = view.findViewById(R.id.locationMc);
        supportMc = view.findViewById(R.id.supportMc);


        packagesMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PackagesActivity.class));
            }
        });

        return  view;
    }
}