package com.example.myview.imgview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

public class NumImageView extends AppCompatImageView {
    public static final String TAG = "NumImageView";

    private Paint mPaint = new Paint();
    private int num = 0;
    private float cx,cy;
    private int radius;
    private int baseY;
    public NumImageView(Context context) {
        super(context);
    }

    public NumImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(int num){
        this.num = num;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(num==0)return;
        mPaint.setColor(Color.parseColor("#d2b8c1"));
        mPaint.setStyle(Paint.Style.FILL);
        int width = getMeasuredWidth();
        Log.e(TAG, "onDraw: width = "+width);
        radius = width/6;
        cx = (width/3)*2+radius;
        cy = radius;
        Log.e(TAG, "onDraw: radius = "+radius);
        Log.e(TAG, "onDraw: cx = "+cx);
        Log.e(TAG, "onDraw: cy = "+cy);
        canvas.drawCircle(cx,cy,radius,mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(radius*4/3);
        mPaint.setTextAlign(Paint.Align.CENTER);
        baseY = (int) ((radius)-((mPaint.descent()+mPaint.ascent())/2));
        canvas.drawText(num+"",cx,baseY,mPaint);
    }
}
