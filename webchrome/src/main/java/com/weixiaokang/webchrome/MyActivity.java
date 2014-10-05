package com.weixiaokang.webchrome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    private Button forwardButton, backButton, gotoButton;
    private EditText urlText;
    private WebView webView;
    private final static String URLS = "http://www.miyijia.com/t/mobileimg";
    private String result = "";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();

        setWebView();

        setOnClickListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                requestsData();
                handler.sendEmptyMessage(305);
            }
        }).start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 305 && !result.equals("")) {
                    Log.i("debug", result);
                }
            }
        };
    }

    private void requestsdataByGet() {
        String string = URLS + "?p=1&num=15";
        try {
            URL url = new URL(string);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String input = null;
            while ((input = bufferedReader.readLine()) != null) {
                result += input + "\n";
            }
            inputStreamReader.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void requestsDataByConnection() {
        try {
            URL url = new URL(URLS);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            StringBuilder params = new StringBuilder();
            params.append("p=")
                    .append("1")
                    .append("&num=")
                    .append("15");
            outputStream.writeBytes(params.toString());
            outputStream.flush();
            outputStream.close();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String string = null;
                while ((string = bufferedReader.readLine()) != null) {
                    result += string + "\n";
                }
                inputStreamReader.close();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void requestsData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(URLS);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("p", "1"));
        params.add(new BasicNameValuePair("num", "15"));
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListener() {
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goForward();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });
        urlText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!"".equals(urlText.getText().toString())) {
                        openBrowser();
                       return true;
                    }
                }
                return false;
            }
        });
        gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(urlText.getText().toString())) {
                    openBrowser();
                } else {
                    showDialog();
                }
            }
        });
    }

    private void setWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
    }

    private void initView() {
        forwardButton = (Button) findViewById(R.id.forward_btn);
        backButton = (Button) findViewById(R.id.goback_btn);
        gotoButton = (Button) findViewById(R.id.goto_btn);
        urlText = (EditText) findViewById(R.id.url_et);
        webView = (WebView) findViewById(R.id.webview);
    }

    private void openBrowser() {
        webView.loadUrl(urlText.getText().toString());
        Toast.makeText(this, "正在加载：" + urlText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        new AlertDialog.Builder(MyActivity.this)
                .setTitle("网页浏览器")
                .setMessage("请输入要访问的网址")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("WebView", "单击确定按钮");
                    }
                }).show();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
