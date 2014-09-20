package com.weixiaokang.webchrome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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


public class MyActivity extends Activity {

    private Button forwardButton, backButton, gotoButton;
    private EditText urlText;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();

        setWebView();

        setOnClickListener();
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
