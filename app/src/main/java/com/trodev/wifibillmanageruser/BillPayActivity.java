package com.trodev.wifibillmanageruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillPayActivity extends AppCompatActivity {

    TextInputEditText nameEt, uidEt, packagesEt, numberEt;
    String userID;
    FirebaseUser user;
    DatabaseReference reference, ref;
    TextView statusTv;
    LottieAnimationView loading_anim;
    MaterialButton payBtn;
    DatabaseReference databaseReference;
    ImageView circleIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay);

        /*back button implement*/
        getSupportActionBar().setTitle("Pay Bill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Editext init*/
        nameEt = findViewById(R.id.nameEt);
        uidEt = findViewById(R.id.uidEt);
        packagesEt = findViewById(R.id.packagesEt);
        numberEt = findViewById(R.id.numberEt);

        /*textview init*/
        statusTv = findViewById(R.id.statusTv);

        /*button init*/
        payBtn = findViewById(R.id.payBtn);

        /*image view init*/
        circleIv = findViewById(R.id.circleIv);

        /*lottie anim init*/
        loading_anim = findViewById(R.id.loading_anim);
        loading_anim.loop(true);

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

                    loading_anim.setVisibility(View.GONE);

                    String uname = userProfile.uname;
                    String num = userProfile.num;
                    String packagess = userProfile.packages;
                    String user_tokes = userProfile.user_token;

                    nameEt.setText(uname);
                    uidEt.setText(user_tokes);
                    numberEt.setText(num);
                    packagesEt.setText(packagess);

                    /*status call*/
                    status_call();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_anim.setVisibility(View.VISIBLE);
                Toast.makeText(BillPayActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


        /*click event*/
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bill_payment();
            }
        });

    }

    private void bill_payment() {

        /*when click button then show animation and toast*/
        Toast.makeText(BillPayActivity.this, "Bill Payment Processing", Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("user_bill");

        String name, number, user_token, packages;

        name = nameEt.getText().toString().trim();
        number = numberEt.getText().toString().trim();
        user_token = uidEt.getText().toString().trim();
        packages = packagesEt.getText().toString().trim();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        String key = databaseReference.push().getKey();


        if (key != null) {
            /*set data on user_status*/
            BillModels billModels = new BillModels(key, name, number, user_token, packages, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.child(key).setValue(billModels);
            Toast.makeText(this, "Bill Payment Complete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }


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
                    statusTv.setText(" Status: " + status);

                    if (status.equals("Inactive")) {

                        statusTv.setTextColor(Color.parseColor("#FF0004"));
                        circleIv.setColorFilter(getApplication().getResources().getColor(R.color.red));
                        Toast.makeText(BillPayActivity.this, "You are " + status + "\nYou can't pay your bill", Toast.LENGTH_SHORT).show();
                        payBtn.setVisibility(View.INVISIBLE);

                    } else if (status.equals("Active")) {

                        statusTv.setTextColor(Color.parseColor("#008937"));
                        circleIv.setColorFilter(getApplication().getResources().getColor(R.color.green));
                        Toast.makeText(BillPayActivity.this, "You are " + status + "\nYou can pay your bill", Toast.LENGTH_SHORT).show();
                        payBtn.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BillPayActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

}