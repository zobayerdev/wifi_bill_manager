package com.trodev.wifibillmanageruser;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InternetSpeedFragment extends Fragment {

    WebView webview;
    private static final String TAG = "AndroidRide";
    private static final String TAGS = "Success";
    String payment_url = "https://fast.com/";

    public InternetSpeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internet_speed, container, false);

        /*init views*/
        webview = view.findViewById(R.id.webview);

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

        return view;
    }
}