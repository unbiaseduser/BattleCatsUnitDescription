package com.sixtyninefourtwenty.bcud.utils.fragments;

import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.SnackbarDuration;
import com.sixtyninefourtwenty.stuff.ToastDuration;

@NonNullTypesByDefault
public sealed interface OnScreenMsgHelper permits BaseBottomSheetAlertDialogFragment, BaseBottomSheetDialogFragment, BaseDialogFragment, BaseFragment {
    default Toast makeToast(int textRes) {
        return makeToast(textRes, ToastDuration.SHORT);
    }
    default Toast makeToast(CharSequence text) {
        return makeToast(text, ToastDuration.SHORT);
    }
    Toast makeToast(int textRes, ToastDuration duration);
    Toast makeToast(CharSequence text, ToastDuration duration);
    default void showToast(int textRes) {
        makeToast(textRes).show();
    }
    default void showToast(CharSequence text) {
        makeToast(text).show();
    }
    default Snackbar makeSnackbar(int textRes) {
        return makeSnackbar(textRes, SnackbarDuration.SHORT);
    }
    default Snackbar makeSnackbar(CharSequence text) {
        return makeSnackbar(text, SnackbarDuration.SHORT);
    }
    Snackbar makeSnackbar(int textRes, SnackbarDuration duration);
    Snackbar makeSnackbar(CharSequence text, SnackbarDuration duration);
    default void showSnackbar(int textRes) {
        makeSnackbar(textRes).show();
    }
}
