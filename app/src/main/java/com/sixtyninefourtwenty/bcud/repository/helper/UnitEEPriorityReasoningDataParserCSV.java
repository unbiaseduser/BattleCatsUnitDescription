package com.sixtyninefourtwenty.bcud.repository.helper;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.common.utils.Validations;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NonNullTypesByDefault
public final class UnitEEPriorityReasoningDataParserCSV implements UnitEEPriorityReasoningSupplier {

    private final ImmutableList<String[]> eepDataFileLines;

    @SneakyThrows
    public UnitEEPriorityReasoningDataParserCSV(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            eepDataFileLines = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .collect(new ImmutableListCollector<>());
        }
    }

    public UnitEEPriorityReasoningDataParserCSV(AssetManager assets) {
        this(AssetsJava.openQuietly(assets, "text/eep_data.txt"));
    }

    @Nullable
    @Override
    public String getPriorityReasoningForUnitWithId(int unitId, ElderEpic elderEpic) {
        final var number = switch (elderEpic) {
            case ELDER -> 0;
            case EPIC -> 1;
        };
        return eepDataFileLines.stream()
                .filter(parts -> Integer.parseInt(parts[0]) == unitId)
                .filter(parts -> Integer.parseInt(parts[1]) == number)
                .findFirst()
                .map(parts -> parts[2])
                .orElse(null);
    }

    @Override
    public ImmutableList<Unit> createElderEpicPriorityList(ElderEpic elderEpic, ImmutableList<Unit> unitsToSearchFrom) {
        final var number = switch (elderEpic) {
            case ELDER -> 0;
            case EPIC -> 1;
        };
        return eepDataFileLines.stream()
                .filter(parts -> Integer.parseInt(parts[1]) == number)
                .map(parts -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == Integer.parseInt(parts[0])))
                .filter(Objects::nonNull)
                .collect(new ImmutableListCollector<>());
    }

}
