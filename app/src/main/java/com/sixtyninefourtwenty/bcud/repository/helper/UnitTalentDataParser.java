package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitTalentData;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;

import org.json.JSONArray;

import java.util.List;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitTalentDataParser implements UnitTalentDataSupplier {

    private final List<UnitTalentData> dataList;

    @SneakyThrows
    public UnitTalentDataParser(String json) {
        dataList = UnitTalentData.SERIALIZER.listFromJson(new JSONArray(json));
    }

    @Override
    public ImmutableList<TalentData> getTalentListForUnitWithId(int id) {
        return dataList.stream()
                .filter(data -> data.getUnitId() == id)
                .findFirst()
                .map(UnitTalentData::getTalents)
                .map(ImmutableList::copyOf)
                .orElse(ImmutableList.of());
    }

    @Override
    public ImmutableList<Unit> createTalentPriorityList(Talent.Priority priority,
                                                        Talent.UnitType type,
                                                        ImmutableList<Unit> unitsToSearchFrom) {
        return dataList.stream()
                .filter(data -> data.getUnitType() == type)
                .mapToInt(UnitTalentData::getUnitId)
                .mapToObj(unitId -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == unitId))
                .filter(Objects::nonNull)
                .filter(unit -> !unit.getTalents(priority).isEmpty())
                .collect(new ImmutableListCollector<>());
    }

}
