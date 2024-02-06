package com.sixtyninefourtwenty.common.objects.repository;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import android.content.res.AssetManager;

import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterial;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.vavr.collection.Stream;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class TFMaterialInfoParser implements TFMaterialInfoSupplier {

    private final List<TFMaterial.Info> materialInfos;

    @SneakyThrows
    public TFMaterialInfoParser(InputStream inputStream) {
        try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            materialInfos = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.CSV_DELIMITER_PIPE))
                    .map(parts -> new TFMaterial.Info(
                            parts[0],
                            Stream.ofAll(List.of(parts))
                                    .drop(1)
                                    .takeWhile(part -> !"＠".equals(part))
                                    .collect(joining(" "))
                    ))
                    .collect(toList());
        }
    }

    public TFMaterialInfoParser(AssetManager assets) {
        this(AssetsJava.openQuietly(assets, "text/GatyaitemName.csv"));
    }

    @Override
    public TFMaterial.Info getInfo(int materialIndex) {
        return materialInfos.get(materialIndex);
    }
}
