package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NonNullTypesByDefault
public final class UnitTalentDataParserCSV implements UnitTalentDataSupplier {

    private final ImmutableList<String[]> tpDataFileLines;

    @SneakyThrows
    public UnitTalentDataParserCSV(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            tpDataFileLines = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .collect(new ImmutableListCollector<>());
        }
    }

    @Override
    public ImmutableList<TalentData> getTalentListForUnitWithId(int id) {
        return tpDataFileLines.stream()
                .filter(parts -> Integer.parseInt(parts[0]) == id)
                .map(parts -> {
                    final var list = new ArrayList<TalentData>();
                    final var priority = Talent.Priority.valueOf(parts[1]);
                    for (int i = 2; i < parts.length; i++) {
                        list.add(new TalentData(
                                Talent.fromIndex(Integer.parseInt(parts[i])),
                                priority));
                    }
                    return list;
                })
                .flatMap(List::stream)
                .collect(new ImmutableListCollector<>());
    }

    @Override
    public ImmutableList<Unit> createTalentPriorityList(Talent.Priority priority,
                                                        Talent.UnitType type,
                                                        ImmutableList<Unit> unitsToSearchFrom) {
        final var unitIds = unitsToSearchFrom.stream()
                .filter(unit -> {
                    final var unitType = unit.getType();
                    return switch (type) {
                        case NON_UBER -> unitType == UnitBaseData.Type.STORY_LEGEND ||
                                unitType == UnitBaseData.Type.CF_SPECIAL ||
                                unitType == UnitBaseData.Type.ADVENT_DROP ||
                                unitType == UnitBaseData.Type.RARE ||
                                unitType == UnitBaseData.Type.SUPER_RARE;
                        case UBER -> unitType == UnitBaseData.Type.UBER ||
                                unitType == UnitBaseData.Type.LEGEND_RARE;
                    };
                })
                .mapToInt(Unit::getId)
                .toArray();
        return tpDataFileLines.stream()
                .filter(parts -> Talent.Priority.valueOf(parts[1]) == priority)
                .filter(parts -> ArraysKt.contains(unitIds, Integer.parseInt(parts[0])))
                .map(parts -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == Integer.parseInt(parts[0])))
                .filter(Objects::nonNull)
                .collect(new ImmutableListCollector<>());
    }

}
