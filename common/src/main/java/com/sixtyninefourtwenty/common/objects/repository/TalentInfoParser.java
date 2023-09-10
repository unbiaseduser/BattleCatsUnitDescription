package com.sixtyninefourtwenty.common.objects.repository;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import android.content.res.AssetManager;

import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.vavr.collection.Stream;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class TalentInfoParser implements TalentInfoSupplier {

    private final List<Talent.Info> talentInfos;

    @SneakyThrows
    public TalentInfoParser(InputStream inputStream) {
        try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            talentInfos = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .map(parts -> new Talent.Info(
                            parts[0],
                            Stream.ofAll(List.of(parts))
                                    .drop(1)
                                    .takeWhile(part -> !"＠".equals(part))
                                    .collect(joining(" "))
                    ))
                    .collect(toList());
        }
    }

    public TalentInfoParser(AssetManager assets) {
        this(AssetsJava.openQuietly(assets, "text/nyankoPictureBook2_en.csv"));
    }

    @Override
    public Talent.Info getInfo(int talentIndex) {
        return talentInfos.get(talentIndex);
    }
}
