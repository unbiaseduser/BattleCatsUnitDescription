package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public sealed interface OnScreenMsgHelper permits BaseBottomSheetAlertDialogFragment, BaseBottomSheetDialogFragment, BaseDialogFragment, BaseFragment {
    default Toast makeToast(int textRes) {
        return makeToast(textRes, Toast.LENGTH_SHORT);
    }
    default Toast makeToast(CharSequence text) {
        return makeToast(text, Toast.LENGTH_SHORT);
    }
    Toast makeToast(int textRes, int duration);
    Toast makeToast(CharSequence text, int duration);
    default void showToast(int textRes) {
        makeToast(textRes).show();
    }
    default void showToast(CharSequence text) {
        makeToast(text).show();
    }
    default Snackbar makeSnackbar(int textRes) {
        return makeSnackbar(textRes, BaseTransientBottomBar.LENGTH_SHORT);
    }
    default Snackbar makeSnackbar(CharSequence text) {
        return makeSnackbar(text, BaseTransientBottomBar.LENGTH_SHORT);
    }
    Snackbar makeSnackbar(int textRes, int duration);
    Snackbar makeSnackbar(CharSequence text, int duration);
    default void showSnackbar(int textRes) {
        makeSnackbar(textRes).show();
    }
}
