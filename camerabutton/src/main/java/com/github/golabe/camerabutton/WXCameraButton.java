package com.github.golabe.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class WXCameraButton extends View {
    private int outsideCircleColor;
    private int innerCircleColor;
    private int progressColor;
    private float progressBorderWidth;
    private static final float DEFAULT_SIZE = 100F;
    private Paint circlePaint;
    private int cx;
    private int cy;
    private int outsideRadius;
    private int innerRadius;
    private GestureDetectorCompat gestureDetector;

    private OnWXTouchListener onWxTouchListener = null;
    private RectF rectF;
    private int expand;
    private ValueAnimator startAnimation;
    private float animatedValue;
    private Paint progressPaint;
    private RectF progressRectF;
    private ValueAnimator progressAnimator;
    private int progressDuration;
    private ValueAnimator endAnimation;
    private float progressValue;

    private enum STATUS {
        NONE, CLICK, LONG_CLICK, END,
    }

    private STATUS status = STATUS.NONE;

    public WXCameraButton(Context context) {
        this(context, null);
    }

    public WXCameraButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WXCameraButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrs(attrs);
        init();
    }

    private void init() {

        rectF = new RectF();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressBorderWidth);

        progressPaint.setStyle(Paint.Style.STROKE);
        progressRectF = new RectF();
        gestureDetector = new GestureDetectorCompat(getContext(), onGestureListener);
    }

    private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            status = STATUS.NONE;
            if (onWxTouchListener != null) {
                onWxTouchListener.onClick();
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            status = STATUS.LONG_CLICK;
            initAnimation();
            startAnimation.start();
            if (onWxTouchListener != null) {
                onWxTouchListener.onLongClick();
            }
        }


    };

    private void attrs(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WXCameraButton);
            outsideCircleColor = a.getColor(R.styleable.WXCameraButton_outsideCircleColor, Color.parseColor("#dddddd"));
            innerCircleColor = a.getColor(R.styleable.WXCameraButton_innerCircleColor, Color.parseColor("#ffffff"));
            progressColor = a.getColor(R.styleable.WXCameraButton_progressColor, Color.parseColor("#82b966"));
            progressBorderWidth = a.getDimension(R.styleable.WXCameraButton_progressBorderWidth, dp2px(4F));
            progressDuration = a.getInt(R.styleable.WXCameraButton_progressDuration, 5000);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec, dp2px(DEFAULT_SIZE));
        int height = measureHeight(heightMeasureSpec, dp2px(DEFAULT_SIZE));
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int gap = w / 20;
        expand = w / 9;
        cx = w / 2;
        cy = w / 2;
        outsideRadius = cx - gap - expand;
        innerRadius = outsideRadius - w / 10;
        rectF.set(gap + expand, gap + expand, w - gap - expand, h - gap - expand);
        float v = progressBorderWidth / 2;
        progressRectF.set(gap + v, gap + v, w - gap - v, h - gap - v);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (status) {
            case NONE:
                circlePaint.setColor(outsideCircleColor);
                canvas.drawCircle(cx, cy, outsideRadius, circlePaint);
                circlePaint.setColor(innerCircleColor);
                canvas.drawCircle(cx, cy, innerRadius, circlePaint);
                break;
            case LONG_CLICK:
                circlePaint.setColor(outsideCircleColor);
                canvas.drawCircle(cx, cy, outsideRadius + animatedValue * expand, circlePaint);
                circlePaint.setColor(innerCircleColor);
                canvas.drawCircle(cx, cy, innerRadius - expand * animatedValue, circlePaint);
                if (!startAnimation.isRunning()) {
                    canvas.drawArc(progressRectF, -90, 360 * progressValue, false, progressPaint);
                }
                break;
            case END:
                circlePaint.setColor(outsideCircleColor);
                canvas.drawCircle(cx, cy, outsideRadius + expand - expand * animatedValue, circlePaint);
                circlePaint.setColor(innerCircleColor);
                canvas.drawCircle(cx, cy, innerRadius - expand + expand * animatedValue, circlePaint);
                break;
        }

    }

    private int measureWidth(int measureSpec, int defaultSize) {
        int result = 0;
        int model = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (model == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = defaultSize + getPaddingLeft() + getPaddingRight();
            if (model == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;

    }

    private int measureHeight(int measureSpec, int defaultSize) {
        int result = 0;
        int model = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (model == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = defaultSize + getPaddingTop() + getPaddingBottom();
            if (model == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x > rectF.left && x < rectF.right && y > rectF.top && y < rectF.bottom) {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (status == STATUS.LONG_CLICK) {
                        status = STATUS.END;
                        startAnimation.cancel();
                        progressAnimator.cancel();
                        endAnimation.start();
                        if (onWxTouchListener != null) {
                            onWxTouchListener.onLongClickUp();
                        }
                    }
                    break;
            }
        }
        return true;
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void setOnWxTouchListener(OnWXTouchListener listener) {
        this.onWxTouchListener = listener;
    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animatedValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    private void initAnimation() {
        startAnimation = ValueAnimator.ofFloat(0, 1);
        startAnimation.setDuration(300);
        startAnimation.setInterpolator(new LinearInterpolator());
        startAnimation.addUpdateListener(updateListener);
        startAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressAnimator.start();
            }
        });

        endAnimation = ValueAnimator.ofFloat(0, 1);
        endAnimation.setDuration(300);
        endAnimation.setInterpolator(new LinearInterpolator());
        endAnimation.addUpdateListener(updateListener);
        endAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS.NONE;
                invalidate();
            }
        });
        progressAnimator = ValueAnimator.ofFloat(0, 1);
        progressAnimator.setDuration(progressDuration);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS.END;
                endAnimation.start();
                if (onWxTouchListener != null && progressValue == 1F) {
                    onWxTouchListener.finish();
                }
            }
        });


    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (startAnimation != null) {
            startAnimation.removeAllUpdateListeners();
            startAnimation.cancel();
        }
        if (endAnimation != null) {
            endAnimation.removeAllUpdateListeners();
            endAnimation.cancel();
        }
        if (progressAnimator != null) {
            progressAnimator.removeAllUpdateListeners();
            progressAnimator.cancel();
        }
    }

}
