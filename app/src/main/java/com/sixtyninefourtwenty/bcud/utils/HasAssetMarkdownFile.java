package com.sixtyninefourtwenty.bcud.utils;

import static com.sixtyninefourtwenty.javastuff.AssetsJava.readEntireTextFile;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.ScrollView;

import com.google.common.cache.Cache;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.Nullable;

import io.noties.markwon.Markwon;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public interface HasAssetMarkdownFile {
    String getFilePath();

    default String readFileContent(AssetManager assets) {
        return readFileContent(assets, Stuff.TEXT_CACHE);
    }

    @SneakyThrows
    default String readFileContent(AssetManager assets, @Nullable Cache<String, String> cache) {
        return cache != null ?
                cache.get(getFilePath(), () -> readEntireTextFile(assets, getFilePath())) :
                readEntireTextFile(assets, getFilePath());
    }

    Markwon createMarkwon(Context context, ScrollView sv);

}
