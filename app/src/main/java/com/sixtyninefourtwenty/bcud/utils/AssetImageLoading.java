package com.sixtyninefourtwenty.bcud.utils;

import static java.util.Objects.requireNonNullElse;

import android.widget.ImageView;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.UnaryOperator;

import coil.Coil;
import coil.request.Disposable;
import coil.request.ImageRequest;

@NonNullTypesByDefault
public final class AssetImageLoading {

    private AssetImageLoading() {
        throw new UnsupportedOperationException();
    }

    private static final String ASSET_URI_PREFIX = "file:///android_asset/";

    @CanIgnoreReturnValue
    public static Disposable loadAssetImage(ImageView iv, String path, @Nullable UnaryOperator<ImageRequest.Builder> config) {
        final var request = new ImageRequest.Builder(iv.getContext())
                .data(ASSET_URI_PREFIX + path)
                .target(iv);
        final var finalConfig = requireNonNullElse(config, UnaryOperator.<ImageRequest.Builder>identity());
        return Coil.imageLoader(iv.getContext()).enqueue(finalConfig.apply(request).build());
    }

    @CanIgnoreReturnValue
    public static Disposable loadAssetImage(ImageView iv, String path) {
        return loadAssetImage(iv, path, null);
    }

}
