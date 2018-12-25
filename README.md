# WXCameraButton
# 防微信拍照录视频按钮

<div align="center"> <image src="https://github.com/Golabe/WXCameraButton/blob/master/gifs/gif.gif?raw=true" width=500/></div>


### gradle

在build.gradle 添加
```xml
implementation 'top.golabe.WXCameraButton:camerabutton:1.0.0'
```

### xml
```xml
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorPrimary"
    tools:context=".MainActivity">

    <com.github.golabe.camerabutton.WXCameraButton
        android:id="@+id/btnWx"
        android:layout_width="120dp"
        android:layout_height="120dp"
        app:progressBorderWidth="6dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:progressDuration="5000" />
</android.support.constraint.ConstraintLayout>
```
### attrs
```xml
<declare-styleable name="WXCameraButton">
        <attr name="outsideCircleColor" format="color" />
        <attr name="innerCircleColor" format="color" />
        <attr name="progressColor" format="color"/>
        <attr name="progressBorderWidth" format="dimension"/>
        <attr name="progressDuration" format="integer"/>
    </declare-styleable>
```


### java
```java
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
```
