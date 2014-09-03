package com.weixiaokang.summerproject.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weixiaokang.summerproject.R;

import java.util.LinkedList;

/**
 * Created by Administrator on 2014/9/2.
 */
public class WeaDataAdapter extends BaseAdapter {

    private Context context;
    private LinkedList<String> data;
    private View views[];
    public WeaDataAdapter(Context context, LinkedList<String> data) {
        this.context = context;
        this.data = data;
        views = new View[data.size()];
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return views[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_list, null);
            TextView weekView = (TextView) view.findViewById(R.id.week_view);
            TextView weatherView = (TextView) view.findViewById(R.id.weather_view);
            TextView tempView = (TextView) view.findViewById(R.id.temp_view);
            weekView.setText(data.get(position * 3));
            weatherView.setText(data.get(position * 3 + 1));
            tempView.setText(data.get(position * 3 + 2));
            views[position] = view;
        } else {
            view = convertView;
        }
        return view;
    }
}
