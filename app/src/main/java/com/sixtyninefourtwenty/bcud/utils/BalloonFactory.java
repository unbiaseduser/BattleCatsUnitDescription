package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;

@NonNullTypesByDefault
public final class BalloonFactory {
    public static Balloon.Builder createWithUsualSettings(Context context) {
        return new Balloon.Builder(context)
                .setTextSize(14)
                .setPadding(10)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR);
    }
}
