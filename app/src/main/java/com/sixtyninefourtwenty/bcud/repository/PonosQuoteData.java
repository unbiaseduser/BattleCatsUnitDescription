package com.sixtyninefourtwenty.bcud.repository;

import static java.util.stream.Collectors.joining;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.vavr.collection.Stream;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@NonNullTypesByDefault
public final class PonosQuoteData implements PonosQuoteSupplier {

    @SneakyThrows
    public PonosQuoteData(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            quotes = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .map(parts -> Stream.ofAll(List.of(parts))
                            .drop(1)
                            .takeWhile(part -> !"＠".equals(part))
                            .collect(joining(" ")))
                    .collect(new ImmutableListCollector<>());
        }
    }

    public PonosQuoteData(AssetManager assets) {
        this(AssetsJava.openQuietly(assets, "text/MainMenu_en.csv"));
    }

    private final ImmutableList<String> quotes;

}
