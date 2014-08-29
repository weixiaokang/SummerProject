package com.weixiaokang.summerproject.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.weixiaokang.summerproject.R;
import com.weixiaokang.summerproject.util.AMapUtil;
import com.weixiaokang.summerproject.util.Constants;
import com.weixiaokang.summerproject.util.WebEncoding;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WeatherActivity extends Activity {

    private static final String TAG = "debug";
    private static final boolean DEBUG = true;
    private static final String PRIVATE_KEY = "cfb26d_SmartWeatherAPI_562f473";
    private static final String APPID = "fe18c6dda88e31c0";
    private static final String PRE_APPID = "fe18c6";
    private String result = "", url = "";
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final TextView textView = (TextView) findViewById(R.id.textview);
        String PUBLIC_KEY = "http://open.weather.com.cn/data/?areaid=101010100&type=alarm&date=201408282155&appid=" + APPID;
        try {
            String key = WebEncoding.urlEncode(PUBLIC_KEY, PRIVATE_KEY);
            if (DEBUG) { Log.i(TAG, "key : "+ key); }
            url = "http://open.weather.com.cn/data/?areaid=101010100&type="+ Constants.INDEX+"&date="+ AMapUtil.getNowTime()+"&appid=" + PRE_APPID + "&key=" + key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "-->go");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (result != null) {
                    textView.setText(result);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest(url);
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }).start();
    }

    private void sendRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.weather, menu);
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
