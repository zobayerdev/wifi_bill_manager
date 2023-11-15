package com.trodev.wifibillmanageruser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.trodev.wifibillmanageruser.R;

public class ReceiptGeneratorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_generator);

        getSupportActionBar().setTitle("Receipt Generator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}