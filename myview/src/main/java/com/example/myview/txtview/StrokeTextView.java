package com.example.myview.txtview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.myview.R;

import java.lang.reflect.Field;

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {
    private TextPaint strokePaint;
    int mInnerColor;
    int mOuterColor;

    public StrokeTextView(Context context) {
        this(context,null);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        strokePaint = this.getPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innerColor,0x000000);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor,0x000000);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean drawSideLine = true;

    @Override
    protected void onDraw(Canvas canvas) {
        if(drawSideLine){
            if(strokePaint == null){
                strokePaint = getPaint();
            }
//            strokePaint.setColor(mOuterColor);
            setTextColorUseReflection(mOuterColor);
            strokePaint.setStrokeWidth(2);
            strokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            strokePaint.setFakeBoldText(true);
            strokePaint.setShadowLayer(1,0,0,0);
            super.onDraw(canvas);

            setTextColorUseReflection(mInnerColor);
            strokePaint.setStrokeWidth(0);
            strokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            strokePaint.setFakeBoldText(false);
            strokePaint.setShadowLayer(0, 0, 0, 0);

        }
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     * @param color
     */
    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        strokePaint.setColor(color);
    }
}
