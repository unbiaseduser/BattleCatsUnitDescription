package com.sixtyninefourtwenty.common.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class NoAutoDismissAlertDialogBuilder {

    private final MaterialAlertDialogBuilder dialogBuilder;
    private boolean dismissOnPositive = true;
    @Nullable
    private DialogInterface.OnClickListener onPositiveButtonClick = null;
    private boolean dismissOnNeutral = true;
    @Nullable
    private DialogInterface.OnClickListener onNeutralButtonClick = null;
    private boolean dismissOnNegative = true;
    @Nullable
    private DialogInterface.OnClickListener onNegativeButtonClick = null;

    public NoAutoDismissAlertDialogBuilder(Context context) {
        dialogBuilder = new MaterialAlertDialogBuilder(context);
    }

    public NoAutoDismissAlertDialogBuilder setTitle(int title) {
        dialogBuilder.setTitle(title);
        return this;
    }

    public NoAutoDismissAlertDialogBuilder setMessage(int message) {
        dialogBuilder.setMessage(message);
        return this;
    }

    public NoAutoDismissAlertDialogBuilder setView(View view) {
        dialogBuilder.setView(view);
        return this;
    }

    public NoAutoDismissAlertDialogBuilder setPositiveButton(int text,
                                                             boolean dismissOnPositive,
                                                             @Nullable DialogInterface.OnClickListener listener) {
        setPositiveState(dismissOnPositive, listener);
        dialogBuilder.setPositiveButton(text, null);
        return this;
    }

    public NoAutoDismissAlertDialogBuilder setPositiveButton(@Nullable CharSequence text,
                                                             boolean dismissOnPositive,
                                                             @Nullable DialogInterface.OnClickListener listener) {
        setPositiveState(dismissOnPositive, listener);
        dialogBuilder.setPositiveButton(text, null);
        return this;
    }

    private void setPositiveState(boolean dismissOnPositive,
                                  @Nullable DialogInterface.OnClickListener listener) {
        this.dismissOnPositive = dismissOnPositive;
        this.onPositiveButtonClick = listener;
    }

    @SuppressWarnings("unused")
    public NoAutoDismissAlertDialogBuilder setNeutralButton(int text,
                                                             boolean dismissOnNeutral,
                                                             @Nullable DialogInterface.OnClickListener listener) {
        setNeutralState(dismissOnNeutral, listener);
        dialogBuilder.setNeutralButton(text, null);
        return this;
    }

    @SuppressWarnings("unused")
    public NoAutoDismissAlertDialogBuilder setNeutralButton(@Nullable CharSequence text,
                                                            boolean dismissOnNeutral,
                                                            @Nullable DialogInterface.OnClickListener listener) {
        setNeutralState(dismissOnNeutral, listener);
        dialogBuilder.setNeutralButton(text, null);
        return this;
    }

    private void setNeutralState(boolean dismissOnNeutral,
                                  @Nullable DialogInterface.OnClickListener listener) {
        this.dismissOnNeutral = dismissOnNeutral;
        this.onNeutralButtonClick = listener;
    }

    public NoAutoDismissAlertDialogBuilder setNegativeButton(int text,
                                                            boolean dismissOnNegative,
                                                            @Nullable DialogInterface.OnClickListener listener) {
        setNegativeState(dismissOnNegative, listener);
        dialogBuilder.setNegativeButton(text, null);
        return this;
    }

    public NoAutoDismissAlertDialogBuilder setNegativeButton(@Nullable CharSequence text,
                                                             boolean dismissOnNegative,
                                                             @Nullable DialogInterface.OnClickListener listener) {
        setNegativeState(dismissOnNegative, listener);
        dialogBuilder.setNegativeButton(text, null);
        return this;
    }

    private void setNegativeState(boolean dismissOnNegative,
                                 @Nullable DialogInterface.OnClickListener listener) {
        this.dismissOnNegative = dismissOnNegative;
        this.onNegativeButtonClick = listener;
    }

    public AlertDialog create() {
        final var dialog = dialogBuilder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (onPositiveButtonClick != null) {
                    onPositiveButtonClick.onClick(d, DialogInterface.BUTTON_POSITIVE);
                }
                if (dismissOnPositive) {
                    d.dismiss();
                }
            });
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
                if (onNeutralButtonClick != null) {
                    onNeutralButtonClick.onClick(d, DialogInterface.BUTTON_NEUTRAL);
                }
                if (dismissOnNeutral) {
                    d.dismiss();
                }
            });
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
                if (onNegativeButtonClick != null) {
                    onNegativeButtonClick.onClick(d, DialogInterface.BUTTON_NEGATIVE);
                }
                if (dismissOnNegative) {
                    d.dismiss();
                }
            });
        });
        return dialog;
    }
}
