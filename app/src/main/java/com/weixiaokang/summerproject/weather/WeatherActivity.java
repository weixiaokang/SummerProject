package com.weixiaokang.summerproject.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weixiaokang.summerproject.R;
import com.weixiaokang.summerproject.util.AMapUtil;
import com.weixiaokang.summerproject.util.Constants;
import com.weixiaokang.summerproject.util.WeatherUtil;
import com.weixiaokang.summerproject.util.WebEncoding;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherActivity extends Activity {

    private static final String TAG = "debug";
    private static final boolean DEBUG = true;
    private static final String PRIVATE_KEY = "cfb26d_SmartWeatherAPI_562f473";
    private static final String APPID = "fe18c6dda88e31c0";
    private static final String PRE_APPID = "fe18c6";
    private String result = "", url = "";
    private String areaid = "101190101", type = Constants.FORECAST1D, date = WeatherUtil.getNowTime();
    private String temp = "";
    private Handler handler;
    private String[] strings = {"one", "two", "three", "four", "five"};
    private ImageView imageView;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (DEBUG) { Log.i(TAG, areaid+"\n" + type + "\n" + date + "\n"); }
        String PUBLIC_KEY = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+type+"&date="+date+"&appid=" + APPID;
        try {
            String key = WebEncoding.urlEncode(PUBLIC_KEY, PRIVATE_KEY);
            if (DEBUG) { Log.i(TAG, "key : "+ key); }
            url = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+ type +"&date="+ date +"&appid=" + PRE_APPID + "&key=" + key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "-->go");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (result != null) {
                    Log.i(TAG, result);
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

    private void initView() {
        imageView = (ImageView) findViewById(R.id.image_in_weather);
        listView = (ListView) findViewById(R.id.listview_in_weather);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, strings);
        listView.setAdapter(adapter);
    }
    private void sendRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                String jsonObject =  EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String string = null;
                while ((string = bufferedReader.readLine()) != null) {
                    Log.i("result", string);
                }
                JSONObject jsonObject = new JSONObject();
            } else {
                result = "请求失败";
            }
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
