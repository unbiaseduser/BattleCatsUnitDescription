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
public final class ComboNameParser implements ComboNameSupplier {

    private final ImmutableList<String> comboNames;

    @SneakyThrows
    public ComboNameParser(InputStream inputStream) {
        try (final var comboNameReader = new BufferedReader(new InputStreamReader(inputStream))) {
            comboNames = comboNameReader.lines()
                    .map(line -> line.substring(0, line.length() - 1))
                    .collect(new ImmutableListCollector<>());
        }
    }

    public ComboNameParser(AssetManager assets) {
        this(Assets.openQuietly(assets, "text/combos/Nyancombo_en.csv"));
    }

    @Override
    public String getName(int comboIndex) {
        return comboNames.get(comboIndex);
    }
}
