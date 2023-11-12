package com.trodev.wifibillmanageruser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.trodev.wifibillmanageruser.R;

public class PackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        /*set title on action bar*/
        getSupportActionBar().setTitle("Our Packages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}