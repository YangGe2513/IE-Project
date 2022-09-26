package com.example.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityDataReportBinding;

public class DataReportActivity extends AppCompatActivity {

    private ActivityDataReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadWebsite();
    }

    public void loadWebsite(){
        // Enable JavaScript
        binding.webView.getSettings().setJavaScriptEnabled(true);

        // Client
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view,url);
            }
        });
        binding.webView.loadUrl(getString(R.string.website));
    }
}