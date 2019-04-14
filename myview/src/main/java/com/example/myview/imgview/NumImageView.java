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
import android.util.Log;

import com.example.myview.R;

public class NumImageView extends AppCompatImageView {
    public static final String TAG = "NumImageView";
    public static final String BG_CIRCLE = "bg_circle";
    public static final String BG_OVAL = "bg_oval";

    private Paint bgPaint = new Paint();
    private Paint textPaint = new Paint();
    private int bgShape = -1;
    private String text = "";
    private float cx,cy;
    private int radius = 0;
    private int noNumRadius = 0;
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

        /**
         *  getDimension 、 getDimensionPixelSize 、getDimensionPixelOffset
         *  如果xml中的值是 X px，则结果为 X ，如果xml中的值是 X dp,则结果为 X 乘屏幕密度
         *  getDimension()是基于当前DisplayMetrics进行转换，获取指定资源id对应的尺寸。文档里并没说这里返回的就是像素，要注意这个函数的返回值是float，像素肯定是int。
         *  getDimensionPixelSize()与getDimension()功能类似，不同的是将结果转换为int，并且小数部分四舍五入。
         *  getDimensionPixelOffset()与getDimension()功能类似，不同的是将结果转换为int，并且偏移转换（offset conversion，函数命名中的offset是这个意思）是直接截断小数位，即取整（其实就是把float强制转化为int，注意不是四舍五入哦）。
         */
        textSize = (int) array.getDimension(R.styleable.NumImageView_textSize,30);
        ovalPadding = (int) array.getDimension(R.styleable.NumImageView_textMargin,6);
        bgColor = array.getColor(R.styleable.NumImageView_pointColor,Color.parseColor("#B71212"));
        textColor = array.getColor(R.styleable.NumImageView_textColor,Color.parseColor("#ffffff"));
        showPoint = array.getBoolean(R.styleable.NumImageView_show,false);
        text = array.getString(R.styleable.NumImageView_pointText);
        if (text ==null){
            text = "";
        }
        noNumRadius = (int) array.getDimension(R.styleable.NumImageView_noNumRadius,0);
        Log.e(TAG, "init: "+noNumRadius);
        bgShape = array.getInteger(R.styleable.NumImageView_bgShape,-1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!showPoint)return;
        initPaint();
        if(text.length()==0){
            // 当text 长度为0时，只画 圆形背景
            drawCircle(canvas);
            return;
        }
        if (bgShape == 0){
            // 指定背景图形为圆形
            drawCircleWithText(canvas);
        }else if (bgShape == 1){
            // 指定背景图形为椭圆
            drawOvalWithText(canvas);
        }else{
            // 没有指定 bgShape
            if(text.length()==1) {
                // text长度为1 ，画圆
                drawCircleWithText(canvas);
            }else{
                // text长度大于1，画椭圆
                drawOvalWithText(canvas);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        width = getMeasuredWidth();
        // 小圆点半径占控件宽度的比例
        if(noNumRadius!=0){
            radius = noNumRadius;
        }else {
            radius = width / 5;
        }
        // 根据半径选择圆点中心点的位置
        cx = width-radius;
        cy = radius;
        canvas.drawCircle(cx,cy,radius, bgPaint);
    }

    private void drawCircleWithText(Canvas canvas) {
        /**
         bgPaint.setColor(Color.parseColor(bgColor));
         bgPaint.setStyle(Paint.Style.FILL);
         width = getMeasuredWidth();
         //        Log.e(TAG, "onDraw: width = "+width);
         // 小圆点半径占控件宽度的比例
         radius = (textSize+ovalPadding)/2;
         */

        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        textPaint.getTextBounds(text,0, text.length(),textRect);
        width = getMeasuredWidth();

        radius = textRect.width()/2 + ovalPadding;

        // 根据半径选择圆点中心点的位置
        cx = width-radius;
        cy = radius;
//        Log.e(TAG, "onDraw: radius = "+radius);
//        Log.e(TAG, "onDraw: cx = "+cx);
//        Log.e(TAG, "onDraw: cy = "+cy);
        canvas.drawCircle(cx,cy,radius, bgPaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        baseY = (int) ((radius) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(text, cx, baseY, textPaint);
    }

    private void drawOvalWithText(Canvas canvas) {
        textPaint.getTextBounds(text,0, text.length(),textRect);
        width = getMeasuredWidth();
        // Right留出 高度的一半来画弧线
        ovalRight = width - textRect.height()/2;
        ovalLeft = (int) (ovalRight - textRect.width());
        ovalTop = 0;
        ovalBottom = ovalTop + textRect.height() + ovalPadding*2;

        bgPath.addArc(new RectF(ovalLeft-textRect.height()/2,ovalTop,ovalLeft+textRect.height()/2,ovalBottom),90,180);
        bgPath.lineTo(ovalRight,ovalTop);
        bgPath.addArc(new RectF(ovalRight-textRect.height()/2,ovalTop,ovalRight+textRect.height()/2,ovalBottom),270,180);
        bgPath.lineTo(ovalLeft,ovalBottom);

        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(bgPath, bgPaint);

        // Paint.Align.LEFT 代表文本被画在中点的 右边
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text + "", ovalLeft, textRect.height()+ovalPadding, textPaint);
    }

    private void initPaint() {
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    public void setBackgroundColor(int backgroundColor){
        this.bgColor = backgroundColor;
        invalidate();
    }

    public void showPoint(String num){
        this.showPoint = true;
        this.text = num;
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

    public void setNomRadius(int radius){
        this.noNumRadius = radius;
        invalidate();
    }
}
