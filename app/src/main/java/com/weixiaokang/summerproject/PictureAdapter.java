package com.weixiaokang.summerproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2014/8/26.
 */
public class PictureAdapter extends BaseAdapter{

    private Context context;
    private View viewArray[];
    public PictureAdapter(Context context) {
        this.context = context;
        viewArray = new View[getCount()];
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return viewArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = layoutInflater.inflate(R.layout.grid_view, null);
            ImageView imageView = (ImageView) gridView.findViewById(R.id.image_in_grid);
            TextView textView = (TextView) gridView.findViewById(R.id.text_in_grid);
            imageView.setImageResource(image[position]);
            textView.setText(text[position]);
            viewArray[position] = gridView;
        } else {
            gridView = convertView;
        }
        return gridView;
    }

    private static final int[] image = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
    private static final int[] text = { R.string.weather_in_grid, R.string.map_in_grid, R.string.app_name, R.string.app_name };
}
