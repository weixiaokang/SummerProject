package com.weixiaokang.summerproject;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

public class MarkerActivity extends Activity implements OnMarkerClickListener , AMapLocationListener, LocationSource {

    private AMap aMap;
	private MapView mapView;
    private Marker marker;
	private LatLng latlng = new LatLng(36.061, 103.834);
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private static final boolean DEBUG = true;
    private static final String TAG = "debug";

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
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);
            aMap.setLocationSource(this);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            aMap.setMyLocationEnabled(true);
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        aMap.setMyLocationStyle(myLocationStyle);
    }
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (DEBUG) {
            Log.i(TAG, "-->onLocationChanged()");
        }
        if (mListener != null && aMapLocation != null) {
            mListener.onLocationChanged(aMapLocation);
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
}
