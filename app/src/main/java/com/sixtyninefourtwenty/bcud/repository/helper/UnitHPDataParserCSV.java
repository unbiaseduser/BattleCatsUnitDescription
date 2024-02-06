package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NonNullTypesByDefault
public final class UnitHPDataParserCSV implements UnitHPDataSupplier {

    private final ImmutableList<String[]> hpDataFileLines;

    @SneakyThrows
    public UnitHPDataParserCSV(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            hpDataFileLines = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.CSV_DELIMITER_PIPE))
                    .collect(new ImmutableListCollector<>());
        }
    }

    @Override
    public ImmutableList<Unit> createHypermaxPriorityList(Hypermax.Priority priority,
                                                          Hypermax.UnitType unitType,
                                                          ImmutableList<Unit> unitsToSearchFrom) {
        final var lineIndex = switch (unitType) {
            case SPECIAL -> switch (priority) {
                case MAX -> 0;
                case HIGH -> 1;
                case MID -> 2;
                case LOW -> 3;
                case MIN -> 4;
            };
            case RARE -> switch (priority) {
                case MAX -> 5;
                case HIGH -> 6;
                case MID -> 7;
                case LOW -> 8;
                case MIN -> 9;
            };
            case SUPER_RARE -> switch (priority) {
                case MAX -> 10;
                case HIGH -> 11;
                case MID -> 12;
                case LOW -> 13;
                case MIN -> 14;
            };
        };

        return Arrays.stream(hpDataFileLines.get(lineIndex))
                .mapToInt(Integer::parseInt)
                .mapToObj(unitId -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == unitId))
                .filter(Objects::nonNull)
                .collect(new ImmutableListCollector<>());
    }

}
