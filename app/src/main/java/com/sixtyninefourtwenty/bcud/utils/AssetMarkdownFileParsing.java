package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;
import android.text.Spanned;

import com.google.common.cache.Cache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.noties.markwon.Markwon;

@NonNullTypesByDefault
public final class AssetMarkdownFileParsing {

    private AssetMarkdownFileParsing() {
        throw new UnsupportedOperationException();
    }

    public static ListenableFuture<Spanned> parseFromObject(ListeningExecutorService executorService,
                                                            HasAssetMarkdownFile subject,
                                                            Context context,
                                                            Markwon markwon) {
        return executorService.submit(() -> {
            final var result = subject.readFileContent(context.getAssets());
            return markwon.toMarkdown(result);
        });
    }

    public static ListenableFuture<Spanned> parseArbitraryFile(ListeningExecutorService executorService,
                                                               Context context,
                                                               String path,
                                                               Markwon markwon,
                                                               @Nullable Cache<String, String> cache) {
        return executorService.submit(() -> {
            final var result = cache != null ? cache.get(path, () -> AssetsJava.readEntireTextFile(context.getAssets(), path)) : AssetsJava.readEntireTextFile(context.getAssets(), path);
            return markwon.toMarkdown(result);
        });
    }

    public static ListenableFuture<Spanned> parseArbitraryFile(ListeningExecutorService executorService,
                                                               Context context,
                                                               String path,
                                                               Markwon markwon) {
        return parseArbitraryFile(executorService, context, path, markwon, Stuff.TEXT_CACHE);
    }

}
