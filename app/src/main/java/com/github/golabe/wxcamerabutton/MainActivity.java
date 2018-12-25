package com.github.golabe.wxcamerabutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.golabe.camerabutton.OnWXTouchListener;
import com.github.golabe.camerabutton.WXCameraButton;

public class MainActivity extends AppCompatActivity {
    private WXCameraButton btnWx;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnWx=findViewById(R.id.btnWx);
        btnWx.setOnWxTouchListener(new OnWXTouchListener() {
            @Override
            public void onClick() {
                Log.d(TAG, "onClick: ");
            }

            @Override
            public void onLongClick() {
                Log.d(TAG, "onLongClick: ");
            }

            @Override
            public void onLongUp() {
                Log.d(TAG, "onLongUp: ");
            }

            @Override
            public void finish() {
                Log.d(TAG, "finish: ");
            }
        });
    }
}
