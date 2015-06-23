package com.kazauskas.ui.widget.spreadbar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kazauskas.ui.widget.spreadbar.R;

public class GradientImageView extends ImageView {
    private Paint mPaint;
    private Float mLeftX;
    private Float mRightX;

    public GradientImageView(Context context) {
        this(context, null);
    }

    public GradientImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.background_gradient_line));
        mLeftX = Float.NaN;
        mRightX = Float.NaN;
    }

    public void setLeftX(Float x) {
        mLeftX = x;
    }

    public void setRightX(Float x) {
        mRightX = x;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!Float.isNaN(mLeftX)) {
            canvas.drawRect(getPaddingLeft(), 0, mLeftX, canvas.getHeight(), mPaint);
        }
        if (!Float.isNaN(mRightX)) {
            canvas.drawRect(mRightX, 0, canvas.getWidth() - getPaddingRight(), canvas.getHeight(), mPaint);
        }
    }
}