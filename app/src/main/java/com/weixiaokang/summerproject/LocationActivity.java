package com.weixiaokang.summerproject;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

import com.amap.api.location.LocationProviderProxy;
import com.weixiaokang.summerproject.util.AMapUtil;
import com.weixiaokang.summerproject.util.ToastUtil;

public class LocationActivity extends Activity implements AMapLocationListener, Runnable{

    private TextView locationView, accuraryView, timeView, descritionView;
    private LocationManagerProxy locationManagerProxy;
    private AMapLocation aMapLocation;
    private Handler handler = new Handler();
    private StringBuilder stringBuilder[];
    private final boolean DEBUG = true;
    private final String TAG = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_location);

        stringBuilder = new StringBuilder[4];
        for (int i = 0; i < stringBuilder.length; i++) {
            stringBuilder[i] = new StringBuilder();
        }
        locationView = (TextView) findViewById(R.id.location_view);
        accuraryView = (TextView) findViewById(R.id.accurary_view);
        timeView = (TextView) findViewById(R.id.time_view);
        descritionView = (TextView) findViewById(R.id.description_view);
        locationManagerProxy = LocationManagerProxy.getInstance(this);
        locationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        handler.postDelayed(this, 12000);
        if (DEBUG) {
            Log.i(TAG, "-->onCreate()");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
        if (DEBUG) {
            Log.i(TAG, "--onPause()");
        }
    }

    private void stopLocation() {
        if (DEBUG) {
            Log.i(TAG, "-->stopLocation");
        }
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            this.aMapLocation = aMapLocation;
            for (int i = 0; i < stringBuilder.length; i++) {
                if (!stringBuilder[i].equals("")) {
                    stringBuilder[i].setLength(0);
                }
            }
            stringBuilder[0].append("经纬度：" + aMapLocation.getLongitude() + "，" + aMapLocation.getLatitude());
            locationView.setText(stringBuilder[0]);
            stringBuilder[1].append("精  度："+aMapLocation.getAccuracy());
            accuraryView.setText(stringBuilder[1]);
            stringBuilder[2].append("时  间："+AMapUtil.convertToTime(aMapLocation.getTime()));
            timeView.setText(stringBuilder[2]);
            stringBuilder[3].append(aMapLocation.getExtras().getString("desc"));
            descritionView.setText(stringBuilder[3]);
            }

        }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void run() {
        if (DEBUG) {
            Log.i(TAG, "-->run()");
        }
        if (aMapLocation == null) {
            ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
            Toast.makeText(this, "12秒内还没有定位成功，停止定位", Toast.LENGTH_LONG).show();
            stopLocation();
        }
    }
}
