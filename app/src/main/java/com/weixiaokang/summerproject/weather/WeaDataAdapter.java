package com.weixiaokang.summerproject.weather;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weixiaokang.summerproject.R;

/**
 * Created by Administrator on 2014/9/2.
 */
public class WeaDataAdapter extends BaseAdapter {

    Context context;
    public WeaDataAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_list, null);
        } else {
            view = convertView;
        }
        return view;
    }
}
