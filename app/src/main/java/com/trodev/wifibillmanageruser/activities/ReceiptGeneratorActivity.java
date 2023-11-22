package com.trodev.wifibillmanageruser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.zxing.WriterException;
import com.trodev.wifibillmanageruser.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReceiptGeneratorActivity extends AppCompatActivity {

    String name, user_id, packages, time, date, month, mobile, year;
    TextView nameTv, uidTv, packagesTv, timeTv, dateTv, monthTv, yearTv,mobileTv, orgTv;
    final static int REQUEST_CODE = 1232;

    MaterialCardView infoLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_generator);

        getSupportActionBar().setTitle("Receipt Generator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*init views*/
        nameTv = findViewById(R.id.nameTv);
        uidTv = findViewById(R.id.uidTv);
        packagesTv = findViewById(R.id.packagesTv);
        timeTv = findViewById(R.id.timeTv);
        dateTv = findViewById(R.id.dateTv);
        monthTv = findViewById(R.id.monthTv);
        mobileTv = findViewById(R.id.mobileTv);
        yearTv = findViewById(R.id.yearTv);
        orgTv = findViewById(R.id.orgTv);

        /*card view init*/
        infoLl = findViewById(R.id.infoLl);

        /*get data from server*/
        name = getIntent().getStringExtra("name");
        user_id = getIntent().getStringExtra("uid");
        packages = getIntent().getStringExtra("package");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        month = getIntent().getStringExtra("month");
        mobile = getIntent().getStringExtra("mobile");
        year = getIntent().getStringExtra("year");

        /*set data on text views*/
        nameTv.setText(name);
        uidTv.setText(user_id);
        packagesTv.setText(packages);
        timeTv.setText(time);
        dateTv.setText(date);
        mobileTv.setText(mobile);
        monthTv.setText(month);
        yearTv.setText("Bill Receipt on "+month +" - " + year);


        /*#----------------------- real time date & time -------------------------*/
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        orgTv.setText(date + " & " + time);

        askPermissions();

    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_download) {
            make_pdf();
            Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    /* ##################################################################### */
    /*qr pdf file*/
    /* ##################################################################### */
    private void make_pdf() {

        DisplayMetrics displayMetrics = new DisplayMetrics();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        infoLl.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        Log.d("my log", "Width Now " + infoLl.getMeasuredWidth());

        // cardView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();

        // Obtain the width and height of the view
        int viewWidth = infoLl.getMeasuredWidth();
        int viewHeight = infoLl.getMeasuredHeight();


        // Create a PageInfo object specifying the page attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas object to draw on the page
        Canvas canvas = page.getCanvas();

        // Create a Paint object for styling the view
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw the view on the canvas
        infoLl.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Specify the path and filename of the output PDF file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /*time wise print*/
        /*eikhane millisecond ta niye lopping vabe pdf banacche*/
        long timestamps = System.currentTimeMillis();
        String fileName = "WBM_" + timestamps + ".pdf";

        File filePath = new File(downloadsDir, fileName);

        try {
            // Save the document to a file
            FileOutputStream fos = new FileOutputStream(filePath);
            document.writeTo(fos);
            document.close();
            fos.close();
            // PDF conversion successful
            Toast.makeText(this, "pdf download successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "pdf download un-successful", Toast.LENGTH_SHORT).show();
        }

    }

}