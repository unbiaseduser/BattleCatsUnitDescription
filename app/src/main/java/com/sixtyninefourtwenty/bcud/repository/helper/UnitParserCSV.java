package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NonNullTypesByDefault
public final class UnitParserCSV implements UnitSupplier {

    private final List<String[]> unitDataFileLines;

    @SneakyThrows
    public UnitParserCSV(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            unitDataFileLines = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .collect(new ImmutableListCollector<>());
        }
    }

    @Override
    public ImmutableList<Unit> createMainList(UnitBaseData.Type type,
                                              IntFunction<ImmutableList<TFMaterialData>> tfMaterialsFunction,
                                              IntFunction<ImmutableList<TalentData>> talentDataFunction,
                                              Function<TalentData, Talent> talentDataToTalentFunction) {

        return unitDataFileLines.stream()
                .filter(parts -> UnitBaseData.Type.valueOf(parts[1]) == type)
                .map(parts -> {
                    final var unitId = Integer.parseInt(parts[0]);
                    return new Unit(
                            unitId,
                            type,
                            tfMaterialsFunction.apply(unitId),
                            talentDataFunction.apply(unitId),
                            talentDataToTalentFunction
                    );
                })
                .collect(new ImmutableListCollector<>());
    }

}
