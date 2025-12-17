package com.example.restaurantmenupos;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String FIREBASE_URL =
            "https://restaurantmenu-7d861.web.app"; // change this

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        setupWebView();

        // Load your Firebase site (or local file if you prefer)
        webView.loadUrl(FIREBASE_URL);

        // Back button behavior
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (webView != null && webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            setEnabled(false);
                            MainActivity.super.onBackPressed();
                        }
                    }
                });
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();

        // You need JS for your app; warning is expected
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        webView.addJavascriptInterface(new WebAppInterface(this), "AndroidApp");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "Page finished loading: " + url);
            }

            // For API 23+ (Android 6 and above)
            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error
            ) {
                // This override itself is only called on API 23+
                if (request != null && request.isForMainFrame()) {
                    String description = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        description = error.getDescription().toString();
                    }

                    Toast.makeText(
                            MainActivity.this,
                            "Main page error: " + description,
                            Toast.LENGTH_LONG
                    ).show();

                    Log.e(TAG, "Main frame error (M+): " + description +
                            " URL=" + request.getUrl());
                } else {
                    Log.w(TAG, "Subresource error (M+): " +
                            (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                                    ? error.getDescription()
                                    : "unknown"));
                }
            }

            // For API 21–22 (old callback, deprecated but still used on those versions)
            @Override
            @SuppressWarnings("deprecation")
            public void onReceivedError(
                    WebView view,
                    int errorCode,
                    String description,
                    String failingUrl
            ) {
                Toast.makeText(
                        MainActivity.this,
                        "Main page error: " + description,
                        Toast.LENGTH_LONG
                ).show();

                Log.e(TAG, "Main frame error (legacy): " + description +
                        " URL=" + failingUrl);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG,
                        "JS console: " +
                                consoleMessage.message() + " @ " +
                                consoleMessage.lineNumber() + " (" +
                                consoleMessage.sourceId() + ")"
                );
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }


    @Override
    protected void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
