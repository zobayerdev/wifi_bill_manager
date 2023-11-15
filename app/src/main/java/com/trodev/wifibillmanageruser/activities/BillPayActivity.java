package com.trodev.wifibillmanageruser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.trodev.wifibillmanageruser.models.BillModels;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillPayActivity extends AppCompatActivity {

    TextInputEditText nameEt, uidEt, packagesEt, numberEt;
    TextView statusTv;
    LottieAnimationView loading_anim;
    MaterialButton payBtn;
    String userID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference reference, ref;
    ImageView circleIv;

    AutoCompleteTextView autoCompleteTextView;

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

        /*adapter view*/
        String[] type = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.drop_down_list, type
        );

        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BillPayActivity.this, autoCompleteTextView.getText().toString() + " select", Toast.LENGTH_SHORT).show();
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

        String name, number, user_token, packages, month;

        name = nameEt.getText().toString().trim();
        number = numberEt.getText().toString().trim();
        user_token = uidEt.getText().toString().trim();
        packages = packagesEt.getText().toString().trim();
        month = autoCompleteTextView.getText().toString().trim();

        if (month.isEmpty()) {
            autoCompleteTextView.setError("please select month");
            autoCompleteTextView.requestFocus();
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
                BillModels billModels = new BillModels(key, name, user_token, packages, number, month, date, time, year,  FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child(key).setValue(billModels);
                Toast.makeText(this, "Bill Payment Complete", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_bill) {

            Toast.makeText(this, "Bill Receipt History", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BillPayActivity.this, BillHistoryActivity.class));

        }
        return true;
    }

}