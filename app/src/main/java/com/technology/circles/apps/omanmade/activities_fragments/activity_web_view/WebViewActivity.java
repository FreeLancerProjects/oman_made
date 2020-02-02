package com.technology.circles.apps.omanmade.activities_fragments.activity_web_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.technology.circles.apps.omanmade.R;
import com.technology.circles.apps.omanmade.databinding.ActivityWebViewBinding;
import com.technology.circles.apps.omanmade.language.LanguageHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;

public class WebViewActivity extends AppCompatActivity {
    private ActivityWebViewBinding binding;
    private String url;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("url"))
        {
            url = intent.getStringExtra("url");
        }
    }

    private void initView() {

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDisplayZoomControls(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);

        new MyAsyncTask().execute(url);



    }

    private class MyAsyncTask extends AsyncTask<String, Void, Document>{
        @Override
        protected Document doInBackground(String... strings) {
            Document document = null;
            try {
                document = Jsoup.connect(strings[0]).get();
                document.getElementsByClass("header").remove();
                document.getElementsByClass("footer").remove();
                document.getElementsByClass("breadcrumb").remove();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            binding.webView.loadDataWithBaseURL(url,document.toString(),"text/html","utf-8","");
            binding.webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

            binding.webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    binding.progBar.setVisibility(View.GONE);

                }
            });


        }
    }
}
