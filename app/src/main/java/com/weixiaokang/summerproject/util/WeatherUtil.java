package com.weixiaokang.summerproject.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2014/8/30.
 */
public class WeatherUtil {
    /**
     * get now time and format the time to weather size
     * @return the now time
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
