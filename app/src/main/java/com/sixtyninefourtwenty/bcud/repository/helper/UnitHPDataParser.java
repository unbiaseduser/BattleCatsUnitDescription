package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.UnitHypermaxData;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import org.json.JSONArray;

import java.util.List;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitHPDataParser implements UnitHPDataSupplier {

    private final List<UnitHypermaxData> dataList;

    @SneakyThrows
    public UnitHPDataParser(String json) {
        dataList = UnitHypermaxData.SERIALIZER.listFromJson(new JSONArray(json));
    }

    @Override
    public ImmutableList<Unit> createHypermaxPriorityList(Hypermax.Priority priority,
                                                          Hypermax.UnitType unitType,
                                                          ImmutableList<Unit> unitsToSearchFrom) {
        return dataList.stream()
                .filter(data -> data.getPriority() == priority && data.getType() == unitType)
                .mapToInt(UnitHypermaxData::getUnitId)
                .mapToObj(unitId -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == unitId))
                .filter(Objects::nonNull)
                .collect(new ImmutableListCollector<>());
    }

}
