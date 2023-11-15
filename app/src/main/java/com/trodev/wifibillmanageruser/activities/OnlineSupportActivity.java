package com.trodev.wifibillmanageruser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.SupportModels;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OnlineSupportActivity extends AppCompatActivity {

    TextInputEditText nameEt, uidEt, numberEt, problemEt;
    TextView statusTv, pstatusTv;
    LottieAnimationView loading_anim;
    MaterialButton submitBtn;
    String userID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference reference, ref;
    ImageView circleIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_support);

        /*back button implement*/
        getSupportActionBar().setTitle("Online Support");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Edit text init*/
        nameEt = findViewById(R.id.nameEt);
        uidEt = findViewById(R.id.uidEt);
        problemEt = findViewById(R.id.problemEt);
        numberEt = findViewById(R.id.numberEt);

        /*textview init*/
        statusTv = findViewById(R.id.statusTv);
        pstatusTv = findViewById(R.id.pstatusTv);

        /*button init*/
        submitBtn = findViewById(R.id.submitBtn);

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
                    String user_tokes = userProfile.user_token;

                    nameEt.setText(uname);
                    uidEt.setText(user_tokes);
                    numberEt.setText(num);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading_anim.setVisibility(View.VISIBLE);
                Toast.makeText(OnlineSupportActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        /*status call*/
        status_call();

        /*click event*/
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bill_payment();
            }
        });
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
                        Toast.makeText(OnlineSupportActivity.this, "You are " + status + "\nYou can't pay your bill", Toast.LENGTH_SHORT).show();
                        submitBtn.setVisibility(View.INVISIBLE);

                    } else if (status.equals("Active")) {

                        statusTv.setTextColor(Color.parseColor("#008937"));
                        circleIv.setColorFilter(getApplication().getResources().getColor(R.color.green));
                        Toast.makeText(OnlineSupportActivity.this, "You are " + status + "\nYou can pay your bill", Toast.LENGTH_SHORT).show();
                        submitBtn.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OnlineSupportActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bill_payment() {

        /*when click button then show animation and toast*/
        Toast.makeText(OnlineSupportActivity.this, "Submit Processing", Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("user_problem");

        String name, number, user_token, problem, statusp;

        name = nameEt.getText().toString().trim();
        number = numberEt.getText().toString().trim();
        user_token = uidEt.getText().toString().trim();
        problem = problemEt.getText().toString().trim();
        statusp = pstatusTv.getText().toString().trim();

        if (problem.isEmpty()) {
            problemEt.setError("please fill problem");
            problemEt.requestFocus();
        } else {


            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = currentDate.format(calForDate.getTime());

            Calendar calForYear = Calendar.getInstance();
            SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
            String year = currentYear.format(calForYear.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            String time = currentTime.format(calForTime.getTime());

            String key = databaseReference.push().getKey();


            if (key != null) {
                /*set data on user_status*/
                SupportModels billModels = new SupportModels(key, name, user_token, number, problem, statusp, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child(key).setValue(billModels);
                Toast.makeText(this, "Submit Complete", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_support, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_support) {
            startActivity(new Intent(OnlineSupportActivity.this, SupportListActivity.class));
        }
        return true;
    }

}