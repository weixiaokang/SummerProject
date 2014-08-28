package com.weixiaokang.summerproject.util;

import android.net.Uri;
import android.util.Base64;

import com.weixiaokang.summerproject.weather.WeatherActivity;

import java.net.URLEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class WebEncoding {

    public static byte[] initHmacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] encodeHmacKey(byte[] data, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encodeBaseHmacKey(byte[] data) throws Exception {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static String urlEncode(String data, String secretKey) throws Exception {
        byte[] key = encodeHmacKey(data.getBytes(), secretKey.getBytes());
        String basekey = encodeBaseHmacKey(key);

        return URLEncoder.encode(basekey.trim(), "UTF-8");
    }
}
