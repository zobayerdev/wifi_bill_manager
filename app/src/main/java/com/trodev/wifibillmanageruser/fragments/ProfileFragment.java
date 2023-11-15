package com.trodev.wifibillmanageruser.fragments;

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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.wifibillmanageruser.activities.PrivacyActivity;
import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.activities.LoginActivity;
import com.trodev.wifibillmanageruser.models.User;


public class ProfileFragment extends Fragment {

    FirebaseUser user;
    DatabaseReference reference, ref;
    String userID;
    ImageView logout;
    ShapeableImageView user_image;

    /*linear layout declare*/
    LinearLayout contactLl, console_ll, rateLl, shareLl, privacyLl;
    LottieAnimationView loading_anim;
    TextView user_status;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*lottie anim init*/
        loading_anim = view.findViewById(R.id.loading_anim);
        loading_anim.loop(true);

        /*init views*/
        TextView nameET = view.findViewById(R.id.nameEt);
        TextView emailET = view.findViewById(R.id.emailTv);
        TextView numberET = view.findViewById(R.id.mobileTv);
        TextView passEt = view.findViewById(R.id.passTv);
        TextView packages = view.findViewById(R.id.packagesTv);
        TextView user_token = view.findViewById(R.id.uidTv);
        TextView nid = view.findViewById(R.id.nidTv);
        user_status = view.findViewById(R.id.statusTv);
        user_image = view.findViewById(R.id.user_image);

        LinearLayout infoLl = view.findViewById(R.id.infoLl);
        infoLl.setVisibility(View.INVISIBLE);


        /*database location and get user uid*/
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                loading_anim.setVisibility(View.VISIBLE);

                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {

                    loading_anim.setVisibility(View.INVISIBLE);
                    infoLl.setVisibility(View.VISIBLE);

                    String uname = userProfile.uname;
                    String email = userProfile.email;
                    String num = userProfile.num;
                    String pass = userProfile.pass;
                    String packagess = userProfile.packages;
                    String nidno = userProfile.nid;
                    String user_tokes = userProfile.user_token;

                    nameET.setText(uname);
                    emailET.setText("E-mail: " + email);
                    numberET.setText("Mobile: " + num);
                    passEt.setText("Password: " + pass);
                    packages.setText("Package: " + packagess);
                    nid.setText("Nid Number: " + nidno);
                    user_token.setText("User ID: " + user_tokes);

                    /*toast sms*/
                    Toast.makeText(getActivity(), uname + " your data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_anim.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        /*status call*/
        status_call();


        /*log out  segment*/
        logout = view.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Toast.makeText(getActivity(), "log-out successful", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
            }
        });

        /*init under layout views*/
        contactLl = view.findViewById(R.id.contactLl);
        console_ll = view.findViewById(R.id.console_ll);
        rateLl = view.findViewById(R.id.rateLl);
        shareLl = view.findViewById(R.id.shareLl);
        privacyLl = view.findViewById(R.id.privacyLl);


        contactLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_email();
            }
        });

        console_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_console();
            }
        });


        rateLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateUsOnGooglePlay();
            }
        });

        shareLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_apps();
            }
        });

        privacyLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PrivacyActivity.class));
            }
        });

        return view;
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
                    user_status.setText("Status: " + status);

                    if (status.equals("Inactive")) {
                        user_status.setTextColor(Color.parseColor("#FF0004"));
                        user_image.setColorFilter(getContext().getResources().getColor(R.color.green));
                    } else if (status.equals("Active")) {
                        user_status.setTextColor(Color.parseColor("#008937"));
                        user_image.setColorFilter(getContext().getResources().getColor(R.color.green));
                    }

                    Toast.makeText(getContext(), "You are " + status + " in our network", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void share_apps() {

        int applicationNameId = getContext().getApplicationInfo().labelRes;
        final String appPackageName = getContext().getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(applicationNameId));
        String text = "Wi-Fi Bill Manager";
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "choose share options"));

    }

    public void rateUsOnGooglePlay() {

        Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Couldn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
        }
    }


    private void go_to_email() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {"zobayer.dev@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact for");
        intent.putExtra(Intent.EXTRA_TEXT, "Assalamualaikum, ");
        intent.putExtra(Intent.EXTRA_CC, "ceo.trodev@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));

    }

    private void go_to_console() {

        // on below line we are creating the uri to open google play store to open google maps application
        Uri uri = Uri.parse("https://play.google.com/store/apps/dev?id=6580660399707616800");
        // initializing intent with action view.
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        // set flags on below line.
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // on below line we are starting the activity.
        startActivity(i);

    }
}