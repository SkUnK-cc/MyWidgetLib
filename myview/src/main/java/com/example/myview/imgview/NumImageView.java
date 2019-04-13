package com.example.myview.imgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.myview.R;

public class NumImageView extends AppCompatImageView {
    public static final String TAG = "NumImageView";

    private Paint mPaint = new Paint();
    private String num = "";
    private float cx,cy;
    private int radius = 0;
    private int baseY;
    private int width;
    private int bgColor ;
    private int textColor ;
    private boolean showPoint = false;
    private int ovalLeft;
    private int ovalTop;
    private int ovalRight;
    private int ovalBottom;
    private int textSize;
    // 字体测量大小Rect
    private Rect textRect = new Rect();
    // 背景path
    private Path bgPath = new Path();
    // 字体距离背景上下边缘的padding
    private int ovalPadding;
    public NumImageView(Context context) {
        super(context);
    }

    public NumImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public NumImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumImageView);

        textSize = (int) array.getDimension(R.styleable.NumImageView_textSize,30);
        ovalPadding = (int) array.getDimension(R.styleable.NumImageView_textMargin,12);
        bgColor = array.getColor(R.styleable.NumImageView_pointColor,Color.parseColor("#B71212"));
        textColor = array.getColor(R.styleable.NumImageView_textColor,Color.parseColor("#ffffff"));
        showPoint = array.getBoolean(R.styleable.NumImageView_show,false);
        num = array.getString(R.styleable.NumImageView_pointText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!showPoint)return;
        if(num.length()==0){
            mPaint.setColor(bgColor);
            mPaint.setStyle(Paint.Style.FILL);
            width = getMeasuredWidth();
//        Log.e(TAG, "onDraw: width = "+width);
            // 小圆点半径占控件宽度的比例
            radius = width/4;
            // 根据半径选择圆点中心点的位置
            cx = width-radius;
            cy = radius;
//        Log.e(TAG, "onDraw: radius = "+radius);
//        Log.e(TAG, "onDraw: cx = "+cx);
//        Log.e(TAG, "onDraw: cy = "+cy);
            canvas.drawCircle(cx,cy,radius,mPaint);
        } else if(num.length()==1) {
            /**
             mPaint.setColor(Color.parseColor(bgColor));
             mPaint.setStyle(Paint.Style.FILL);
             width = getMeasuredWidth();
             //        Log.e(TAG, "onDraw: width = "+width);
             // 小圆点半径占控件宽度的比例
             radius = (textSize+ovalPadding)/2;
             */

            mPaint.setTextSize(textSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(bgColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.getTextBounds(num,0,num.length(),textRect);
            width = getMeasuredWidth();

            radius = textRect.width()+ ovalPadding;

            // 根据半径选择圆点中心点的位置
            cx = width-radius;
            cy = radius;
//        Log.e(TAG, "onDraw: radius = "+radius);
//        Log.e(TAG, "onDraw: cx = "+cx);
//        Log.e(TAG, "onDraw: cy = "+cy);
            canvas.drawCircle(cx,cy,radius,mPaint);

            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            baseY = (int) ((radius) - ((mPaint.descent() + mPaint.ascent()) / 2));
            canvas.drawText(num, cx, baseY, mPaint);
        }else{
//            numString = String.valueOf(num);
            mPaint.setTextSize(textSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(num,0,num.length(),textRect);
            width = getMeasuredWidth();
            // Right留出 高度的一半来画弧线
            ovalRight = width - textRect.height()/2;
            ovalLeft = (int) (ovalRight - textRect.width());
            ovalTop = 0;
            ovalBottom = ovalTop + textRect.height() + ovalPadding;

            bgPath.addArc(new RectF(ovalLeft-textRect.height()/2,ovalTop,ovalLeft+textRect.height()/2,ovalBottom),90,180);
            bgPath.lineTo(ovalRight,ovalTop);
            bgPath.addArc(new RectF(ovalRight-textRect.height()/2,ovalTop,ovalRight+textRect.height()/2,ovalBottom),270,180);
            bgPath.lineTo(ovalLeft,ovalBottom);

            mPaint.setColor(bgColor);
            mPaint.setStyle(Paint.Style.FILL);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                canvas.drawOval(ovalLeft,ovalTop,ovalRight,ovalBottom,mPaint);
//            }
            canvas.drawPath(bgPath,mPaint);

            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            // Paint.Align.LEFT 代表文本被画在中点的 右边
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(num + "", ovalLeft, textRect.height()+ovalPadding/2, mPaint);
        }
    }

    public void setBackgroundColor(int backgroundColor){
        this.bgColor = backgroundColor;
        invalidate();
    }

    public void showPoint(String num){
        this.showPoint = true;
        this.num = num;
        invalidate();
    }

    public void hidePoint(){
        this.showPoint = false;
        invalidate();
    }

    public void setTextSize(int px){
        this.textSize = px;
        invalidate();
    }

    public void setOvalPadding(int padding){
        this.ovalPadding = padding;
        invalidate();
    }
}
