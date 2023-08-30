package com.sixtyninefourtwenty.bcuddatagenerator.objects;

import androidx.annotation.StringRes;
import androidx.navigation.NavDirections;

import lombok.Value;

@Value
public class NavigationItem {
    @StringRes
    int title;
    NavDirections directions;
}
