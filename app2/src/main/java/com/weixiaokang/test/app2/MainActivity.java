package com.weixiaokang.test.app2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String PUBLIC_KEY = "http://open.weather.com.cn/data/?areaid=101010100&type=observe&date=201408282155&appid=fe18c6dda88e31c0";
        String PRIVATE_KEY = "cfb26d_SmartWeatherAPI_562f473";
        SecretKey secretKey = new SecretKeySpec(PRIVATE_KEY.getBytes(), "HmacSHA1");
        try {
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] byteKey = mac.doFinal(PUBLIC_KEY.getBytes());
            String baseKey = Base64.encodeToString(byteKey, Base64.DEFAULT);
            Log.i("encode", baseKey);
            String urlKey = URLEncoder.encode(baseKey.trim(), "UTF-8");
            Log.i("encode", urlKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
