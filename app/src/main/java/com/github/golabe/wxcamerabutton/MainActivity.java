package com.github.golabe.wxcamerabutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.golabe.camerabutton.OnWXTouchListener;
import com.github.golabe.camerabutton.WXCameraButton;

public class MainActivity extends AppCompatActivity {
    private WXCameraButton btnWx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnWx=findViewById(R.id.btnWx);
        btnWx.setOnWxTouchListener(new OnWXTouchListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick() {
                Toast.makeText(MainActivity.this,"onLongClick",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickUp() {
                Toast.makeText(MainActivity.this,"onLongClickUp",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void finish() {
                Toast.makeText(MainActivity.this,"finish",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
