package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import lombok.Value;

@NonNullTypesByDefault
public final class BitmapExport {

    private BitmapExport() {}

    @Value
    public static class Input {
        Bitmap bitmap;
        Bitmap.CompressFormat format;
        int quality;
    }

    public static ListenableFuture<Boolean> exportBitmap(ListeningExecutorService executorService, Uri uri, Context context, Input input) {
        return executorService.submit(() -> {
            final var stream = context.getContentResolver().openOutputStream(uri);
            if (stream != null) {
                try (stream) {
                    return input.getBitmap().compress(input.getFormat(), input.getQuality(), stream);
                }
            } else return false;
        });
    }


}
