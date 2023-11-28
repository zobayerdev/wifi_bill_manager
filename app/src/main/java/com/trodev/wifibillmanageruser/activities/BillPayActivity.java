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
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

public class BillPayActivity extends AppCompatActivity {

    TextInputEditText nameEt, uidEt, priceEt, numberEt;
    TextView statusTv, packageTv;
    LottieAnimationView loading_anim;
    MaterialButton payBtn;
    String userID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference reference, ref;
    ImageView circleIv, circlesIv;
    AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pay);

        /*back button implement*/
        getSupportActionBar().setTitle("Pay Bill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Edit text init*/
        nameEt = findViewById(R.id.nameEt);
        uidEt = findViewById(R.id.uidEt);
        priceEt = findViewById(R.id.priceEt);
        numberEt = findViewById(R.id.numberEt);

        /*textview init*/
        statusTv = findViewById(R.id.statusTv);
        packageTv = findViewById(R.id.packageTv);

        /*button init*/
        payBtn = findViewById(R.id.payBtn);

        /*image view init*/
        circleIv = findViewById(R.id.circleIv);
        circlesIv = findViewById(R.id.circlesIv);


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
                    String price = userProfile.prices;
                    String user_tokes = userProfile.user_token;
                    String packages = userProfile.packages;

                    nameEt.setText(uname);
                    uidEt.setText(user_tokes);
                    numberEt.setText(num);
                    priceEt.setText(price);
                    packageTv.setText(packages);

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


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_payment_info();

            }
        });

    }

    private void send_payment_info() {

        String name, number, user_token, price, month, packages;

        name = nameEt.getText().toString().trim();
        number = numberEt.getText().toString().trim();
        user_token = uidEt.getText().toString().trim();
        price = priceEt.getText().toString().trim();
        packages = packageTv.getText().toString().trim();
        month = autoCompleteTextView.getText().toString().trim();

        if (month.isEmpty()) {
            autoCompleteTextView.setError("please select month");
            autoCompleteTextView.requestFocus();
        } else {
            Intent intent = new Intent(BillPayActivity.this, BillWebActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("number", number);
            intent.putExtra("uid", user_token);
            intent.putExtra("price", price);
            intent.putExtra("package", packages);
            intent.putExtra("month", month);
            startActivity(intent);
            // startActivity(new Intent(BillPayActivity.this, BillWebActivity.class));
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
                        packageTv.setTextColor(Color.parseColor("#FF0004"));
                        circleIv.setColorFilter(getApplication().getResources().getColor(R.color.red));
                        circlesIv.setColorFilter(getApplication().getResources().getColor(R.color.red));
                        Toast.makeText(BillPayActivity.this, "You are " + status + "\nYou can't pay your bill", Toast.LENGTH_SHORT).show();
                        payBtn.setVisibility(View.INVISIBLE);

                    } else if (status.equals("Active")) {

                        statusTv.setTextColor(Color.parseColor("#008937"));
                        packageTv.setTextColor(Color.parseColor("#008937"));
                        circleIv.setColorFilter(getApplication().getResources().getColor(R.color.green));
                        circlesIv.setColorFilter(getApplication().getResources().getColor(R.color.green));
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