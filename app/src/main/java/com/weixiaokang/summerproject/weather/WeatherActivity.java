package com.weixiaokang.summerproject.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.weixiaokang.summerproject.R;
import com.weixiaokang.summerproject.util.Constants;
import com.weixiaokang.summerproject.util.WeatherUtil;
import com.weixiaokang.summerproject.util.WebEncoding;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherActivity extends Activity {

    private static final String TAG = "debug";
    private static final boolean DEBUG = true;
    private static final String PRIVATE_KEY = "cfb26d_SmartWeatherAPI_562f473";
    private static final String APPID = "fe18c6dda88e31c0";
    private static final String PRE_APPID = "fe18c6";
    private String result_l = "", result_cf = "", result_i = "", url_l = "", url_cf = "", url_i = "";
    private String areaid = "101190101", date = WeatherUtil.getNowTime();
    private String temp = "";
    private Handler handler;
//    private String[] strings = {"one", "two", "three", "four", "five"};
    private ImageView imageView;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        initView();
        String PUBLIC_KEY_L = "http://open.weather.com.cn/data/?areaid=" + areaid + "&type=" + Constants.OBSERVE + "&date=" + date + "&appid=" + APPID;
        String PUBLIC_KEY_C_F = "http://open.weather.com.cn/data/?areaid=" + areaid + "&type=" + Constants.FORECAST3D + "&date=" + date + "&appid=" + APPID;
        String PUBLIC_KEY_I = "http://open.weather.com.cn/data/?areaid=" + areaid + "&type=" + Constants.INDEX + "&date=" + date + "&appid=" + APPID;
        try {
            String KEY_L = WebEncoding.urlEncode(PUBLIC_KEY_L, PRIVATE_KEY);
            String KEY_C_F = WebEncoding.urlEncode(PUBLIC_KEY_C_F, PRIVATE_KEY);
            String KEY_I = WebEncoding.urlEncode(PUBLIC_KEY_I, PRIVATE_KEY);
            url_l = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+ Constants.OBSERVE +"&date="+ date +"&appid=" + PRE_APPID + "&key=" + KEY_L;
            url_cf = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+ Constants.FORECAST3D +"&date="+ date +"&appid=" + PRE_APPID + "&key=" + KEY_C_F;
            url_i = "http://open.weather.com.cn/data/?areaid="+areaid+"&type="+ Constants.INDEX +"&date="+ date +"&appid=" + PRE_APPID + "&key=" + KEY_I;
            if (DEBUG) { Log.i(TAG, "\n" + url_l + "\n" + url_cf + "\n" + url_i); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "-->go");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (result_l != null) {
                    Log.i(TAG, result_l);
                }
                if (result_cf != null) {
                    Log.i(TAG, result_cf);
                }
                if (result_i != null) {
                    Log.i(TAG, result_i);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (DEBUG) { Log.i(TAG, "-->prepare to sendRequest"); }
                sendRequest();
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }).start();
    }

/*    private void initView() {
        imageView = (ImageView) findViewById(R.id.image_in_weather);
        listView = (ListView) findViewById(R.id.listview_in_weather);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, strings);
        listView.setAdapter(adapter);
    }*/

    private void sendRequest() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest_l = new HttpGet(url_l);
        HttpGet httpRequest_cf = new HttpGet(url_cf);
        HttpGet httpRequest_i = new HttpGet(url_i);
        HttpResponse httpResponse_l, httpResponse_cf, httpResponse_i;
        if (DEBUG) { Log.i(TAG, "-->prepare to send"); }
        try {
            httpResponse_l = httpClient.execute(httpRequest_l);
            if (DEBUG) { Log.i(TAG, "-->have got httpResponse_l"); }
            if (httpResponse_l.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result_l =  EntityUtils.toString(httpResponse_l.getEntity(), "UTF-8");
            } else {
               result_l = "实况请求失败";
            }
            httpResponse_cf = httpClient.execute(httpRequest_cf);
            if (DEBUG) { Log.i(TAG, "-->have got httpResponse_cf"); }
            if (httpResponse_cf.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result_cf = EntityUtils.toString(httpResponse_cf.getEntity(), "UTF-8");
            } else {
                result_cf = "预报请求失败";
            }
            httpResponse_i = httpClient.execute(httpRequest_i);
            if (DEBUG) { Log.i(TAG, "-->have got httpResponse_i"); }
            if (httpResponse_i.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result_i = EntityUtils.toString(httpResponse_i.getEntity(), "UTF-8");
            } else {
                result_i = "指数请求失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (DEBUG) { Log.i(TAG, "-->IOException"); }
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
