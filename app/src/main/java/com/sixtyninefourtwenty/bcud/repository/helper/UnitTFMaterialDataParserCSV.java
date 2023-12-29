package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.konloch.util.FastStringUtils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.utils.CommonConstants;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NonNullTypesByDefault
public final class UnitTFMaterialDataParserCSV implements UnitTFMaterialDataSupplier {

    private final ImmutableList<String[]> materialDataFileLines;

    @SneakyThrows
    public UnitTFMaterialDataParserCSV(InputStream input) {
        try (final var reader = new BufferedReader(new InputStreamReader(input))) {
            materialDataFileLines = reader.lines()
                    .map(line -> FastStringUtils.split(line, CommonConstants.PIPE))
                    .collect(new ImmutableListCollector<>());
        }
    }

    @Override
    public ImmutableList<TFMaterialData> getMaterialListForUnitWithId(int id) {
        return materialDataFileLines.stream()
                .filter(parts -> Integer.parseInt(parts[0]) == id)
                .findFirst()
                .map(parts -> {
                    final var materials = new ArrayList<TFMaterialData>();
                    for (int i = 1; i < parts.length; i += 2) {
                        materials.add(new TFMaterialData(
                                Integer.parseInt(parts[i]),
                                Integer.parseInt(parts[i + 1])
                        ));
                    }
                    return materials;
                })
                .map(ImmutableList::copyOf)
                .orElse(ImmutableList.of());

    }

}
