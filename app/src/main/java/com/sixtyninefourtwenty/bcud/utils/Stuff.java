package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.enums.License;
import com.sixtyninefourtwenty.bcud.objects.ThirdPartyLibInfo;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import kotlin.io.TextStreamsKt;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class Stuff {

    private Stuff() {
        throw new UnsupportedOperationException();
    }

    public static final ImmutableList<ThirdPartyLibInfo> LIB_INFOS = ImmutableList.of(
            new ThirdPartyLibInfo("AndroidFastScroll", new ThirdPartyLibInfo.Dev("Google LLC", "https://github.com/zhanghai"),
                    License.APACHE2, ImmutableList.of("https://github.com/zhanghai/AndroidFastScroll")),
            new ThirdPartyLibInfo("Balloon", new ThirdPartyLibInfo.Dev("skydoves (Jaewoong Eum)", "https://github.com/skydoves"),
                    License.APACHE2, ImmutableList.of("https://github.com/skydoves/Balloon")),
            new ThirdPartyLibInfo("BetterLinkMovementMethod", new ThirdPartyLibInfo.Dev("Saket Narayan", "https://github.com/saket"),
                    License.APACHE2, ImmutableList.of("https://github.com/saket/Better-Link-Movement-Method")),
            new ThirdPartyLibInfo("Checker Framework", new ThirdPartyLibInfo.Dev("The Checker Framework developers", "https://checkerframework.org/"),
                    License.GPL2_CE, ImmutableList.of("https://checkerframework.org/", "https://github.com/typetools/checker-framework/")),
            new ThirdPartyLibInfo("Coil", new ThirdPartyLibInfo.Dev("Coil Contributors", "https://github.com/coil-kt"),
                    License.APACHE2, ImmutableList.of("https://coil-kt.github.io/coil/", "https://github.com/coil-kt/coil")),
            new ThirdPartyLibInfo("Duration Humanizer", new ThirdPartyLibInfo.Dev("mirrajabi", "https://github.com/mirrajabi"),
                    License.UNLICENSE, ImmutableList.of("https://github.com/mirrajabi/duration-humanizer")),
            new ThirdPartyLibInfo("ExpandableLayout", new ThirdPartyLibInfo.Dev("Daniel Cachapa", "https://github.com/cachapa"),
                    License.APACHE2, ImmutableList.of("https://github.com/cachapa/ExpandableLayout")),
            new ThirdPartyLibInfo("FastUtil", new ThirdPartyLibInfo.Dev("vigna", "https://github.com/vigna"),
                    License.APACHE2, ImmutableList.of("https://fastutil.di.unimi.it/", "https://github.com/vigna/fastutil")),
            new ThirdPartyLibInfo("FilledBoxSpinner", new ThirdPartyLibInfo.Dev("Kojo Fosu Bempa Edue", "https://github.com/kojofosu"),
                    License.MIT, ImmutableList.of("https://github.com/kojofosu/FilledBoxSpinner")),
            new ThirdPartyLibInfo("FullDraggableDrawer", new ThirdPartyLibInfo.Dev("Drakeet Xu", "https://github.com/PureWriter"),
                    License.APACHE2, ImmutableList.of("https://github.com/PureWriter/FullDraggableDrawer")),
            new ThirdPartyLibInfo("Guava", new ThirdPartyLibInfo.Dev("Google Inc.", "https://github.com/google"),
                    License.APACHE2, ImmutableList.of("https://github.com/google/guava")),
            new ThirdPartyLibInfo("Markwon", new ThirdPartyLibInfo.Dev("Dimitry Ivanov", "https://noties.io/"),
                    License.APACHE2, ImmutableList.of("https://noties.io/Markwon/", "https://github.com/noties/Markwon")),
            new ThirdPartyLibInfo("MaterialBanner", new ThirdPartyLibInfo.Dev("Sergey Ivanov", "https://github.com/sergivonavi"),
                    License.APACHE2, ImmutableList.of("https://github.com/sergivonavi/MaterialBanner")),
            new ThirdPartyLibInfo("Material Popup Menu", new ThirdPartyLibInfo.Dev("zawadz88", "https://github.com/zawadz88"),
                    License.APACHE2, ImmutableList.of("https://github.com/zawadz88/MaterialPopupMenu")),
            new ThirdPartyLibInfo("Moshi", new ThirdPartyLibInfo.Dev("Square, Inc.", "https://square.github.io/"),
                    License.APACHE2, ImmutableList.of("https://square.github.io/moshi/1.x/", "https://github.com/square/moshi")),
            new ThirdPartyLibInfo("PhotoEditor", new ThirdPartyLibInfo.Dev("Burhanuddin Rashid", "https://burhanrashid52.com/"),
                    License.MIT, ImmutableList.of("https://github.com/burhanrashid52/PhotoEditor")),
            new ThirdPartyLibInfo("SwipeToActionLayout", new ThirdPartyLibInfo.Dev("Alexander Dadukin", "http://st235.xyz/"),
                    License.MIT, ImmutableList.of("https://github.com/st235/SwipeToActionLayout"))
    );

    public static final Cache<String, String> TEXT_CACHE = CacheBuilder.newBuilder().maximumSize(15).build();

    @SneakyThrows
    public static String getText(String path, Context context, @Nullable Cache<String, String> textCache) {
        final var finalCache = textCache != null ? textCache : TEXT_CACHE;
        return finalCache.get(path, () -> {
            try (final var reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path)))) {
                return TextStreamsKt.readText(reader);
            }
        });
    }

    public static String getText(String path, Context context) {
        return getText(path, context, null);
    }

}
