package com.sixtyninefourtwenty.common.objects;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ElderEpic {
    ELDER(R.string.elder), EPIC(R.string.epic);

    @StringRes
    private final int text;
}
