package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import org.json.JSONArray;

import java.util.List;
import java.util.function.IntFunction;

import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitParser implements UnitSupplier {

    private final List<UnitBaseData> dataList;

    @SneakyThrows
    public UnitParser(String json) {
        dataList = UnitBaseData.SERIALIZER.listFromJson(new JSONArray(json));
    }

    @Override
    public ImmutableList<Unit> createMainList(UnitBaseData.Type type,
                                              IntFunction<ImmutableList<TFMaterialData>> tfMaterialsFunction,
                                              IntFunction<ImmutableList<TalentData>> talentDataFunction) {
        return dataList.stream()
                .filter(data -> data.getType() == type)
                .map(data -> new Unit(
                        data.getUnitId(),
                        data.getType(),
                        tfMaterialsFunction.apply(data.getUnitId()),
                        talentDataFunction.apply(data.getUnitId())
                ))
                .collect(new ImmutableListCollector<>());
    }

}
