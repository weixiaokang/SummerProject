package com.weixiaokang.summerproject.area;

import com.amap.api.maps.model.LatLngBounds;
import com.weixiaokang.summerproject.util.Constants;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2014/8/25.
 */
public class Area {

    private HashMap<LatLngBounds, String> llbs;
    public Area() {
        llbs = new HashMap<LatLngBounds, String>();
        llbs.put(Constants.JIAOSI, "教四");
    }
    public HashMap<LatLngBounds, String> getLlbs() {
        return llbs;
    }
}
