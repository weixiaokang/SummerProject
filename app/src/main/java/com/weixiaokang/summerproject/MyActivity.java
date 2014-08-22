package com.weixiaokang.summerproject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyActivity extends Activity implements LocationSource, AMapLocationListener{

    private Button myLocation;
    private TextView longitude, latitude;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private TileOverlay tileOverlay;
    private Marker marker;

    private final boolean DEBUG = true;
    private final String TAG = "debug";
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
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (DEBUG) {
            Log.i(TAG, "-->activate(), -->onLocationChanged()");
        }
        mListener = onLocationChangedListener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        if (DEBUG) {
            Log.i(TAG, "-->deactivate()");
        }
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (DEBUG) {
            Log.i(TAG, "-->onLocationChanged()");
        }
        if (mListener != null && aMapLocation != null) {
            mListener.onLocationChanged(aMapLocation);
            marker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation
                    .getLongitude()));
            longitude.setText(aMapLocation.getLongitude()+"");
            latitude.setText(aMapLocation.getLatitude()+"");
            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(118.933651, 32.109086), 16));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void init() {
        if (aMap == null) {
            if (DEBUG) {
                Log.i(TAG, "-->init()");
            }
            aMap = mapView.getMap();
            ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
            giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
            marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icons(giflist).period(50));
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource
                    (R.drawable.location_marker));
            myLocationStyle.strokeColor(Color.BLACK);
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 128));
            myLocationStyle.strokeWidth(0.1f);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationRotateAngle(180);
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            AssetManager assetManager = getAssets();
            saveTileToSD();
            try {
                InputStream inputStream = assetManager.open("njupt.jpg");

            } catch (IOException e) {
                e.printStackTrace();
            }
            myLocation = (Button) findViewById(R.id.my_location);
            longitude = (TextView) findViewById(R.id.longitude);
            latitude = (TextView) findViewById(R.id.latitude);
        }
    }

    private void saveTileToSD() {
        File rootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "njupt");
        if (!rootDir.mkdirs()) {
            Log.i(TAG, "can't create directory");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return item.getItemId() == R.id.action_settings||super.onOptionsItemSelected(item);
    }
}
