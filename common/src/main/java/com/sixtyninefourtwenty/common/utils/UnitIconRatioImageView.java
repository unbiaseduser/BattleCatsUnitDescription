package com.sixtyninefourtwenty.common.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ImageView that displays images having 110:85 aspect ratio.
 */
public final class UnitIconRatioImageView extends AbstractRatioImageView {

    public UnitIconRatioImageView(@NonNull Context context) {
        super(context);
    }

    public UnitIconRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnitIconRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected double getWidthToHeightRatio() {
        return 110D / 85;
    }
}
