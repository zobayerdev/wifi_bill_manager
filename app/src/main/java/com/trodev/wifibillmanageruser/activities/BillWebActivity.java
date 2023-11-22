package com.trodev.wifibillmanageruser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.BillModels;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillWebActivity extends AppCompatActivity {

    private static final String TAG = "AndroidRide";
    private static final String TAGS = "Success";
    String success_url = "https://shop.bkash.com/trodev01777614837/pay/payment-success";
    String payment_url = "https://shop.bkash.com/trodev01777614837/pay/bdt1/mYstke";
    WebView webview;
    String name, number, user_token, packages, month;
    DatabaseReference databaseReference;
    MaterialButton payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_web);

        /*init views*/
        webview = findViewById(R.id.webview);
        payBtn = findViewById(R.id.payBtn);

        // ###########################################################
        // WebSite Address Here
        webview.loadUrl(payment_url);

        //############################################################

        // if you set this size in your website, Fixed it or don't use this
        webview.setInitialScale(90);

        //#############################################################
        // Website Zoom control
        // webview.getSettings().setBuiltInZoomControls(true);


        //#############################################################
        // extra Code of webview
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "Started: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "Finished: " + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                Log.e(TAG, "Commit: " + url);
                super.onPageCommitVisible(view, url);
            }

        });

        WebSettings mywebsetting = webview.getSettings();

        mywebsetting.setJavaScriptEnabled(true);

        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setDatabaseEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mywebsetting.setDomStorageEnabled(true);
        mywebsetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mywebsetting.setUseWideViewPort(true);
        mywebsetting.setSavePassword(true);
        mywebsetting.setSaveFormData(true);
        mywebsetting.setEnableSmoothTransition(true);


        // ############################## Download Code is Here ####################################
        // Download any File in this website.
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //Checking runtime permission for devices above Marshmallow.
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    downloadDialog(url, userAgent, contentDisposition, mimetype);
                } else {
                    Log.v(TAG, "Permission is revoked");
                    //requesting permissions.
                    ActivityCompat.requestPermissions(BillWebActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        /*bill payment*/
                        bill_payment();
                    }
                }, 30000);

            }
        });

    }

    private void bill_payment() {

        /*when click button then show animation and toast*/
        Toast.makeText(BillWebActivity.this, "Bill Payment Processing", Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("user_bill");

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        user_token = getIntent().getStringExtra("uid");
        packages = getIntent().getStringExtra("package");
        month = getIntent().getStringExtra("month");

        if (user_token.isEmpty()) {

        } else {


            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            String time = currentTime.format(calForTime.getTime());

            Calendar calForYear = Calendar.getInstance();
            SimpleDateFormat currentYear = new SimpleDateFormat("yyyy");
            String year = currentYear.format(calForYear.getTime());



            String key = databaseReference.push().getKey();


            if (key != null) {
                /*set data on user_status*/
                BillModels billModels = new BillModels(key, name, user_token, packages, number, month, date, time, year, FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child(key).setValue(billModels);
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {

        //getting filename from url.
        final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);

        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(BillWebActivity.this);

        //title of alertdialog
        builder.setTitle("Download File");

        //message of alertdialog
        builder.setMessage("Do you want to download " + filename);

        //if Yes button clicks.
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //DownloadManager.Request created with url.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                //cookie
                String cookie = CookieManager.getInstance().getCookie(url);

                //Add cookie and User-Agent to request
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", userAgent);

                //file scanned by Media Scannar
                request.allowScanningByMediaScanner();

                //Download is visible and its progress, after completion too.
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                //DownloadManager created
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                //Saving files in Download folder
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                //download enqued
                downloadManager.enqueue(request);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //cancel the dialog if Cancel clicks
                dialog.cancel();
            }
        });

        //alertdialog shows.
        builder.create().show();
    }

    // One-BackPress Method is here....
    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {

            webview.goBack();
            webview.clearCache(true);

        } else {

            super.onBackPressed();

        }
    }

}