package com.sixtyninefourtwenty.bcud.repository.helper;

import static java.util.Objects.requireNonNull;

import android.content.res.AssetManager;

import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitDescPageTextsParserCSV implements UnitDescPageTextsSupplier {

    private final Int2ObjectMap<Unit.DescPageTexts> data = new Int2ObjectOpenHashMap<>();

    @SneakyThrows
    public UnitDescPageTextsParserCSV(InputStream inputStream) {

        Function<String, @Nullable String> normalizeCSVValue = input -> !input.equals("-") ? input : null;

        try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .forEach(line -> data.put(Integer.parseInt(line[0]), Unit.DescPageTexts.of(
                            normalizeCSVValue.apply(line[1]),
                            normalizeCSVValue.apply(line[2]),
                            normalizeCSVValue.apply(line[3])
                    )));
        }
    }

    public UnitDescPageTextsParserCSV(AssetManager assets) {
        this(AssetsJava.openQuietly(assets, "text/unit_desc_page_texts.txt"));
    }

    @Override
    public Unit.DescPageTexts getDescPageTexts(int unitId) {
        return requireNonNull(data.getOrDefault(unitId, Unit.DescPageTexts.EMPTY));
    }

}
