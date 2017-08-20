package com.example.daim.vkandrarchex.screen.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.example.daim.vkandrarchex.AppDelegate;
import com.example.daim.vkandrarchex.R;
import com.example.daim.vkandrarchex.screen.gallery.GalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements AuthView{

    private static final String LOG_TAG = "AuthActivity";

    @BindView(R.id.login_web_view)
    WebView mWebView;

    private AuthPresenter mPresenter;

    public static void start(){
        Intent intent = new Intent(AppDelegate.getAppContext(), AuthActivity.class);
        AppDelegate.getAppContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mWebView.setWebViewClient(new AuthWebClient());

        mPresenter = new AuthPresenter(this);
        mPresenter.init();
    }

    @Override
    public void loadAuthWebView(String url) {
        mWebView.loadUrl(url);
        Log.d(LOG_TAG, url);
    }

    @Override
    public void openGalleryScreen() {
        GalleryActivity.start(this);
        finish();
    }

    private class AuthWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(LOG_TAG, "onPageFinishied:" + url);
            mPresenter.saveParseToken(url);
        }
    }

}
