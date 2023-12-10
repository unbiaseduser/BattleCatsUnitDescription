package com.sixtyninefourtwenty.common.objects;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class Hypermax {

    private Hypermax() {}

    @Getter
    @AllArgsConstructor
    public enum Priority {
        MAX(R.string.max_priority), HIGH(R.string.high_priority), MID(R.string.medium_priority), LOW(R.string.low_priority), MIN(R.string.min_priority);

        @StringRes
        private final int text;
    }

    @Getter
    @AllArgsConstructor
    public enum UnitType {
        SPECIAL(R.string.special), RARE(R.string.rare), SUPER_RARE(R.string.super_rare);

        @StringRes
        private final int text;
    }
}
