package com.trodev.wifibillmanageruser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trodev.wifibillmanageruser.MainActivity;
import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.StatusModel;
import com.trodev.wifibillmanageruser.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginTv;
    MaterialButton login;
    EditText nameET, mobileET, emailET, passET, nidEt, uidEt, packageEt;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    AutoCompleteTextView autoCompleteTextView, statusTv;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        /*hide action bar*/
        getSupportActionBar().hide();

        /*auth profile from firebase*/
        mAuth = FirebaseAuth.getInstance();

        /*init widget views*/
        login = findViewById(R.id.log_Btn);
        login.setOnClickListener(this);
        nameET = findViewById(R.id.nameEt);
        mobileET = findViewById(R.id.numberEt);
        emailET = findViewById(R.id.emailEt);
        passET = findViewById(R.id.passEt);
        nidEt = findViewById(R.id.nidEt);
        uidEt = findViewById(R.id.uidEt);
        progressBar = findViewById(R.id.progressBar);


        /*adapter view*/
        String[] type = {"Minor-500", "Junior-650", "Learner-800", "Basic-1000", "Primary-1200", "Dominant-1500", "Confident-2000", "Positive-2500", "Advance-3000", "Progressive-4000"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.drop_down_list, type
        );

        autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SignupActivity.this, autoCompleteTextView.getText().toString() + " select", Toast.LENGTH_SHORT).show();
            }
        });

        /*adapter view*/
        String[] types = {"Active", "Inactive"};
        ArrayAdapter<String> adapters = new ArrayAdapter<>(
                this, R.layout.drop_down_list, types
        );

        statusTv = findViewById(R.id.statusTv);
        statusTv.setAdapter(adapters);
        statusTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SignupActivity.this, statusTv.getText().toString() + " select", Toast.LENGTH_SHORT).show();
            }
        });

        loginTv = findViewById(R.id.login);
        loginTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int itemId = view.getId();

        if (itemId == R.id.log_Btn) {
            registarUser();
        } else if (itemId == R.id.login) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            Toast.makeText(SignupActivity.this, "Sign in Here", Toast.LENGTH_SHORT).show();
        }

    }

    private void registarUser() {

        String name, number, email, pass, nid, user_toke, packages, status;
        name = nameET.getText().toString().trim();
        number = mobileET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        pass = passET.getText().toString().trim();
        nid = nidEt.getText().toString().trim();
        user_toke = uidEt.getText().toString().trim();
        packages = autoCompleteTextView.getText().toString().trim();
        status = statusTv.getText().toString().trim();

        if (name.isEmpty()) {
            nameET.setError("Name is required");
            nameET.requestFocus();
            return;
        }
        if (number.isEmpty()) {
            mobileET.setError("Mobile number is required");
            mobileET.requestFocus();
            return;
        }

        if (nid.isEmpty()) {
            nidEt.setError("NID number is required");
            nidEt.requestFocus();
            return;
        }

        if (user_toke.isEmpty()) {
            uidEt.setError("User ID is required");
            uidEt.requestFocus();
            return;
        }


        if (packages.isEmpty()) {
            packageEt.setError("Packages is required");
            packageEt.requestFocus();
            return;
        }

        if (status.isEmpty()) {
            statusTv.setError("Status is required");
            statusTv.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailET.setError("E-mail is required");
            emailET.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please provide valid email!");
            emailET.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            passET.setError("Password is required");
            passET.requestFocus();
            return;
        }

        if (pass.length() <= 6) {
            passET.setError("Min password length should be 6 character");
            passET.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User user = new User(name, number, email, pass, user_toke, nid, packages);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(SignupActivity.this, "User Registration Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                saveto_user_list();
                                                finish();

                                            } else {

                                                progressBar.setVisibility(View.VISIBLE);
                                                Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }
                    }
                });


    }

    private void saveto_user_list() {

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("user_status");

        String status, name, number, user_token, packages;

        status = statusTv.getText().toString().trim();
        name = nameET.getText().toString().trim();
        number = mobileET.getText().toString().trim();
        user_token = uidEt.getText().toString().trim();
        packages = autoCompleteTextView.getText().toString().trim();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());


        /*set data on user_status*/
        StatusModel statusModel = new StatusModel(status, name, number, user_token, packages, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(statusModel);

/*        String Key = databaseReference.push().getKey();
        if (Key != null) {

            StatusModel statusModel = new StatusModel(Key, status, name, number, user_token, packages, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid());

            *//*these data save on new uid and also user id*//*
            *//*these data save on user id*//*
            databaseReference.child(Key).setValue(statusModel);


            Toast.makeText(this, "user activation successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }*/


    }
}