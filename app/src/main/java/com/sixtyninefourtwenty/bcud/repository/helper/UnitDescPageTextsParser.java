package com.sixtyninefourtwenty.bcud.repository.helper;

import android.content.res.AssetManager;

import com.google.common.io.CharStreams;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitDescPageTextsParser implements UnitDescPageTextsSupplier {

    private final Int2ObjectMap<Unit.DescPageTexts> data = new Int2ObjectOpenHashMap<>();

    @SneakyThrows
    @SuppressWarnings("unused")
    public UnitDescPageTextsParser(String json) {
        UnitBaseData.SERIALIZER.listFromJson(new JSONArray(json))
                .forEach(d -> data.put(d.getUnitId(), new Unit.DescPageTexts(
                        d.getTextForInfo(UnitBaseData.Info.USEFUL_TO_OWN),
                        d.getTextForInfo(UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT),
                        d.getTextForInfo(UnitBaseData.Info.HYPERMAX_PRIORITY)
                )));
    }

    @SneakyThrows
    public UnitDescPageTextsParser(AssetManager assets) {
        try (final var reader = new BufferedReader(new InputStreamReader(assets.open("text/unit_base_data.json")))) {
            final var json = CharStreams.toString(reader);
            UnitBaseData.SERIALIZER.listFromJson(new JSONArray(json))
                    .forEach(d -> data.put(d.getUnitId(), new Unit.DescPageTexts(
                            d.getTextForInfo(UnitBaseData.Info.USEFUL_TO_OWN),
                            d.getTextForInfo(UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT),
                            d.getTextForInfo(UnitBaseData.Info.HYPERMAX_PRIORITY)
                    )));
        }
    }

    @Override
    public Unit.DescPageTexts getDescPageTexts(int unitId) {
        return data.getOrDefault(unitId, Unit.DescPageTexts.EMPTY);
    }
}
