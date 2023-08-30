package com.sixtyninefourtwenty.bcud.utils.fragments;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public sealed interface NavigationHelper permits BaseBottomSheetAlertDialogFragment, BaseBottomSheetDialogFragment, BaseDialogFragment, BaseFragment {
    NavController getNavController();
    default void navigate(NavDirections direction) {
        getNavController().navigate(direction);
    }
}
