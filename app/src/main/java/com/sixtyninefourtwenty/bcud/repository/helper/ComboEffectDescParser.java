package com.sixtyninefourtwenty.bcud.repository.helper;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.javastuff.Assets;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class ComboEffectDescParser implements ComboEffectDescSupplier {

    private final ImmutableList<String> typeEffectLines;
    private final ImmutableList<String> levelEffectLines;

    @SneakyThrows
    public ComboEffectDescParser(InputStream typeLinesFile,
                                 InputStream levelLinesFile) {
        try (final var typeLinesFileReader = new BufferedReader(new InputStreamReader(typeLinesFile));
             final var levelEffectLinesReader = new BufferedReader(new InputStreamReader(levelLinesFile))) {
            typeEffectLines = typeLinesFileReader.lines()
                    .map(line -> line.substring(0, line.length() - 1))
                    .collect(new ImmutableListCollector<>());
            levelEffectLines = levelEffectLinesReader.lines()
                    .map(line -> line.substring(0, line.length() - 1))
                    .collect(new ImmutableListCollector<>());
        }
    }

    public ComboEffectDescParser(AssetManager assets) {
        this(Assets.openQuietly(assets, "text/combos/Nyancombo1_en.csv"), Assets.openQuietly(assets, "text/combos/Nyancombo2_en.csv"));
    }

    @Override
    public String getDesc(int typeIndex, int levelIndex) {
        return typeEffectLines.get(typeIndex) + levelEffectLines.get(levelIndex);
    }
}
