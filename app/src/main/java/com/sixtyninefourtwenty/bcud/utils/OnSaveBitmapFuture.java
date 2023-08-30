package com.sixtyninefourtwenty.bcud.utils;

import android.graphics.Bitmap;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;

public interface OnSaveBitmapFuture extends OnSaveBitmap {

    void handleFuture(ListenableFuture<Bitmap> future);

    @Override
    default void onBitmapReady(Bitmap bitmap) {
        final var future = SettableFuture.<Bitmap>create();
        future.set(bitmap);
        handleFuture(future);
    }

    @Override
    default void onFailure(Exception e) {
        final var future = SettableFuture.<Bitmap>create();
        future.setException(e);
        handleFuture(future);
    }
}
