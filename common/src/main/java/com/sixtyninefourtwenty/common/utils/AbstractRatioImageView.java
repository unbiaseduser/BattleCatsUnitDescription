package com.sixtyninefourtwenty.common.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public abstract sealed class AbstractRatioImageView extends AppCompatImageView permits SquareImageView, UnitIconRatioImageView {

    protected AbstractRatioImageView(@NonNull Context context) {
        super(context);
    }

    protected AbstractRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected AbstractRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() / getWidthToHeightRatio()));
    }

    protected abstract double getWidthToHeightRatio();
}
