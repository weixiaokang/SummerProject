package com.weixiaokang.summerproject.location;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.weixiaokang.summerproject.R;
import com.weixiaokang.summerproject.area.Area;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.weixiaokang.summerproject.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyActivity extends Activity implements LocationSource, AMapLocationListener
        , AMap.OnMapTouchListener, AMap.OnCameraChangeListener, OnMarkerClickListener {

    private Button myLocation;
    private TextView longitude, latitude;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker[] markers= new Marker[2];
//    private TileOverlay tileOverlay;
    private Marker marker, me;
    private Point touchLocation = new Point();
    private boolean isInit = true;
    private Area area = new Area();

    private final boolean DEBUG = true;
    private final String TAG = "debug";
//    private GroundOverlay school;
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
            if (isInit) { initMyLocation(aMapLocation); }
//            longitude.setText(aMapLocation.getLongitude()+"");
//            latitude.setText(aMapLocation.getLatitude()+"");
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * initialize the aMap show on screen, marker look like lader,
     * button for refresh the location information
     * amap bind the location connecter.
     */
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
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
            myLocationStyle.strokeColor(Color.BLACK);
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 128));
            myLocationStyle.strokeWidth(0.1f);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationRotateAngle(180);
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMapTouchListener(this);
            aMap.setOnMarkerClickListener(this);

            myLocation = (Button) findViewById(R.id.my_location);
            longitude = (TextView) findViewById(R.id.longitude);
            latitude = (TextView) findViewById(R.id.latitude);

            me = aMap.addMarker(new MarkerOptions()
                    .title("me").snippet("here").anchor(0.5f, 0.5f).position(Constants.BEIJING)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            addMarkers();
            /*LatLngBounds latLngBounds = LatLngBounds.builder()
                                        .include(new LatLng(32.104276, 118.927915))
                                        .include(new LatLng(32.121051, 118.932853)).build();
            school = aMap.addGroundOverlay(new GroundOverlayOptions()
                                               .anchor(0.5f, 0.5f)
                                               .transparency(0.1f)
                                               .image(BitmapDescriptorFactory.fromAsset("njupt.jpg"))
                                               .positionFromBounds(latLngBounds));*/
        }
    }

    private void initMyLocation(AMapLocation aMapLocation) {
        LatLng initLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(initLatLng).tilt(0).zoom(16).build();
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        isInit = false;
    }

    private void addMarkers() {
        markers[0] = aMap.addMarker(new MarkerOptions()
        .anchor(0.5f, 0.5f).position(new LatLng(32.108233, 118.930888))
        .title("教四").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers[1] = aMap.addMarker(new MarkerOptions()
        .anchor(0.5f, 0.5f).position(new LatLng(32.110817, 118.932966))
        .title("兰苑").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        clearMarkers();
    }

    private void displayMarkers() {
        for (int i = 0; i < markers.length; i++) {
            markers[i].setVisible(true);
        }
    }
    private void clearMarkers() {
        for (int i = 0; i < markers.length; i++) {
            markers[i].setVisible(false);
        }
    }
/*    private void saveTileToSD() throws IOException{
        AssetManager assetManager = getAssets();
        File rootDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "njupt");
        if (!rootDir.mkdirs()) {
            Log.i(TAG, "can't create directory");
        }
            File file = new File(rootDir.getPath()+File.separator+"father.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = assetManager.open("njupt.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
    }*/

 /*   private void pasteTile() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(118.933651, 32.109086), 16));
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int i, int i2, int i3) {
                try {
                    String s = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "father.png";
                    Log.i(TAG, s);
                    return new URL("file://" + s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.i(TAG, "URLException");
                }
                return null;
            }
        };
        tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider)
                .diskCacheDir("/storage/amap/cache")
                .diskCacheEnabled(true)
                .diskCacheSize(100));
    }*/

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
        switch (item.getItemId()) {
            case R.id.add_marker:
                displayMarkers();
                break;
            case R.id.clear_marker:
                clearMarkers();
                break;
        }
        return false;
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (DEBUG) { Log.i(TAG, "-->onTouch()"); }
        int x = (int)event.getX();
        int y = (int)event.getY();
        touchLocation.set(x, y);
        Projection projection = aMap.getProjection();
        LatLng latLng = projection.fromScreenLocation(touchLocation);
        latitude.setText(""+latLng.latitude);
        longitude.setText(""+latLng.longitude);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (DEBUG) { Log.i(TAG, "-->onCameraChange()"); }
        float zoom = aMap.getCameraPosition().zoom;
        Toast.makeText(this, ""+zoom, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (DEBUG) { Log.i(TAG, "-->onMarkerClick()"); }
        marker.showInfoWindow();
        return false;
    }
}