package com.aderayanbima31.masigit;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;



public class JadwalShalatFragment extends Fragment {

    ProgressBar pb_per;
    //WebView mWebView;
    View view;


    public JadwalShalatFragment() {
        // Required empty public constructor
    }

    public WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_jadwal_shalat, container, false);
        mWebView = (WebView) v.findViewById(R.id.webjadwal_shalat);
        mWebView.loadUrl("https://www.jadwalsholat.org/adzan/monthly.php?id=14");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;


    }
}
