package com.sixtyninefourtwenty.common.objects;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ElderEpic {
    ELDER(R.string.elder, 0), EPIC(R.string.epic, 1);

    @StringRes
    private final int text;
    private final int numberInCSV;
}
