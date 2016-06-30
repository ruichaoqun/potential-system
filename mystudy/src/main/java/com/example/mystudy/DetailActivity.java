package com.example.mystudy;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.squareup.picasso.Picasso;

import utils.ImageloadFresco;

public class DetailActivity extends BaseActivity {
    private SimpleDraweeView collapsing_img;
    private WebView webView;
    private MagicProgressBar progressBar;

    @Override
    int getLayoutId() {return R.layout.activity_detail;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String webUrl = intent.getStringExtra("WEB_URL");
        String webWho = intent.getStringExtra("WEB_WHO");
        String girlUrl = intent.getStringExtra("GIRL_URL");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsing_img = (SimpleDraweeView) findViewById(R.id.collapsing_img);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(!webWho.isEmpty()){
            toolbarLayout.setTitle(webWho);
        }
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.accent));


        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.ic_toys_black_48dp));
        new ImageloadFresco.ImageloadFrescoBuilder(girlUrl,this,collapsing_img)
                .setRetryImage(drawable)
                //.setIsFaceDetact(true)
                .build();
        /*//加载头部图片
        Picasso.with(this)
                .load(girlUrl)
                .error(R.mipmap.error)
                .centerCrop()
                .fit()
                .into(collapsing_img);*/


        webView = (WebView) findViewById(R.id.webview);
        progressBar = (MagicProgressBar) findViewById(R.id.progressbar);
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webUrl);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                webView.setVisibility(View.GONE);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri content_url=  Uri.parse(webUrl) ;
                Intent ie = new Intent(Intent.ACTION_VIEW,content_url);
                startActivity(ie);
            }
        });
    }

    @Override
    void initResAndListener() {

    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setPercent((float)newProgress/(float)100);
            if (newProgress == 1) {
                progressBar.setVisibility(View.GONE);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }
    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            return;
        }
        webView.setVisibility(View.GONE);
        super.onBackPressed();
    }

}
