package com.weixiaokang.summerproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.weixiaokang.summerproject.guide.WebGuide;
import com.weixiaokang.summerproject.location.MyActivity;
import com.weixiaokang.summerproject.weather.WeatherActivity;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.main_view);
        gridView.setAdapter(new PictureAdapter(this));
        gridView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MainActivity.this, WebGuide.class);
                startActivity(intent);
                break;
            case 3:
                finish();
                break;
            default:
                break;
        }
    }
}
