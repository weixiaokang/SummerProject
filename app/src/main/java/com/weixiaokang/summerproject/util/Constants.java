package com.weixiaokang.summerproject.util;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;

import java.util.Collection;
import java.util.LinkedList;

public class Constants {

	public static final int ERROR = 1001;// 网络异常
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// 路径规划中公交模式
	public static final int ROUTE_DRIVING_RESULT = 2003;// 路径规划中驾车模式
	public static final int ROUTE_WALK_RESULT = 2004;// 路径规划中步行模式
	public static final int ROUTE_NO_RESULT = 2005;// 路径规划没有搜索到结果

	public static final int POISEARCH = 4000;// poi搜索到结果
	public static final int POISEARCH_NO_RESULT = 4001;// poi没有搜索到结果
	public static final int POISEARCH_NEXT = 5000;// poi搜索下一页

	public static final int BUSLINE_LINE_RESULT = 6001;// 公交线路查询
	public static final int BUSLINE_id_RESULT = 6002;// 公交id查询
	public static final int BUSLINE_NO_RESULT = 6003;// 异常情况

    /**
     * weather data type
     */
    public static final String OBSERVE = "observe";
    public static final String ALARM = "warm";
    public static final String INDEX = "index";
    public static final String FORECAST1D = "forecast3d";

    public static final LatLngBounds XIAOYUAN = LatLngBounds.builder()
            .include(new LatLng(32.104583, 118.928036))
            .include(new LatLng(32.105493, 118.930846))
            .include(new LatLng(32.117522, 118.924278))
            .include(new LatLng(32.121528, 118.932916))
            .build();
    public static final LatLngBounds JIAOSI = LatLngBounds.builder()
            .include(new LatLng(32.108258, 118.930435))
            .include(new LatLng(32.108776, 118.931371))
            .build();
}
