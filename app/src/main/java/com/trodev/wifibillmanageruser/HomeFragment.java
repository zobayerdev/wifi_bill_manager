package com.trodev.wifibillmanageruser;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.wifibillmanageruser.activities.BillHistoryActivity;
import com.trodev.wifibillmanageruser.activities.BillPayActivity;
import com.trodev.wifibillmanageruser.activities.OnlineSupportActivity;
import com.trodev.wifibillmanageruser.activities.PackagesActivity;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

public class HomeFragment extends Fragment {

    MaterialCardView packagesMc, monthlyMc, payMc, billHistoryMc, locationMc, supportMc;
    LinearLayout infoLl;
    TextView nameTv, useridTv, packageTv, dateTv, statusTv, appTv, priceTv;
    ImageView circleIv;
    String userID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference reference, ref;
    LottieAnimationView loading_anim;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*lottie anim init*/
        loading_anim = view.findViewById(R.id.loading_anim);
        loading_anim.loop(true);

        /*init card view*/
        packagesMc = view.findViewById(R.id.packagesMc);
        monthlyMc = view.findViewById(R.id.monthlyMc);
        payMc = view.findViewById(R.id.payMc);
        billHistoryMc = view.findViewById(R.id.bilHistoryMc);
        locationMc = view.findViewById(R.id.locationMc);
        supportMc = view.findViewById(R.id.supportMc);
        infoLl = view.findViewById(R.id.infoLl);
        infoLl.setVisibility(View.INVISIBLE);

        /*init text view*/
        nameTv = view.findViewById(R.id.nameTv);
        useridTv = view.findViewById(R.id.useridTv);
        packageTv = view.findViewById(R.id.packageTv);
        dateTv = view.findViewById(R.id.dateTv);
        statusTv = view.findViewById(R.id.statusTv);
        priceTv = view.findViewById(R.id.priceTv);

        appTv = view.findViewById(R.id.appTv);
        appTv.setVisibility(View.VISIBLE);

        /*imageview init*/
        circleIv = view.findViewById(R.id.circleIv);

        /*database location and get user uid*/
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        /*show user profile data*/
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                loading_anim.setVisibility(View.VISIBLE);
                loading_anim.setVisibility(View.INVISIBLE);

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {

                    loading_anim.setVisibility(View.GONE);
                    infoLl.setVisibility(View.VISIBLE);

                    String uname = userProfile.uname;
                    String packagess = userProfile.packages;
                    String user_tokes = userProfile.user_token;
                    String price = userProfile.prices;


                    nameTv.setText(uname);
                    useridTv.setText(user_tokes);
                    packageTv.setText(packagess);
                    priceTv.setText(price + " ৳");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_anim.setVisibility(View.VISIBLE);
                infoLl.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


        /*status call*/
        status_call();

        packagesMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PackagesActivity.class));
            }
        });

        monthlyMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        payMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PayBillActivity.class));
            }
        });

        billHistoryMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BillHistoryActivity.class));
            }
        });

        locationMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });

        supportMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OnlineSupportActivity.class));
            }
        });

        return view;
    }

    private void openMap() {
        
        Uri uri = Uri.parse("geo:0, 0?q= 23.912193, 90.400509");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }

    private void status_call() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        /*show single value from database*/
        ref = FirebaseDatabase.getInstance().getReference("user_status");
        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StatusModel userProfile = snapshot.getValue(StatusModel.class);

                if (userProfile != null) {

                    String status = userProfile.getStatus();
                    String date = userProfile.getDate();
                    statusTv.setText(" Status: " + status);
                    dateTv.setText(date);

                    if (status.equals("Inactive")) {

                        statusTv.setTextColor(Color.parseColor("#FF0004"));
                        circleIv.setColorFilter(getContext().getResources().getColor(R.color.red));

                    } else if (status.equals("Active")) {

                        statusTv.setTextColor(Color.parseColor("#008937"));
                        circleIv.setColorFilter(getContext().getResources().getColor(R.color.green));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

}