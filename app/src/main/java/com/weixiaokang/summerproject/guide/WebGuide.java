package com.weixiaokang.summerproject.guide;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weixiaokang.summerproject.R;


public class WebGuide extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_web);

        WebView webView = (WebView) findViewById(R.id.web_location);
        webView.loadUrl("http://mo.amap.com/navi/?start=118.931033,32.106522&dest=118.93316,32.109898&destName=兰苑&key=f1fe4983a92cee2d1440e9930b8f49a2");
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
