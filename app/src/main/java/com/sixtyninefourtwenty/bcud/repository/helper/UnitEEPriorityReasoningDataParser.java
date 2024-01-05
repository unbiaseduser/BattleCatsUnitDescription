package com.sixtyninefourtwenty.bcud.repository.helper;

import android.content.res.AssetManager;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.UnitEEPriorityData;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.common.utils.Validations;
import com.sixtyninefourtwenty.javastuff.AssetsJava;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONArray;

import java.util.List;
import java.util.Objects;

import kotlin.collections.CollectionsKt;
import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitEEPriorityReasoningDataParser implements UnitEEPriorityReasoningSupplier {

    private final List<UnitEEPriorityData> dataList;

    @SneakyThrows
    public UnitEEPriorityReasoningDataParser(String json) {
        dataList = UnitEEPriorityData.SERIALIZER.listFromJson(new JSONArray(json));
    }

    public UnitEEPriorityReasoningDataParser(AssetManager assets) {
        this(AssetsJava.readEntireTextFile(assets,"text/unit_eep_data.json"));
    }

    @Nullable
    @Override
    public String getPriorityReasoningForUnitWithId(int unitId, ElderEpic elderEpic) {
        return dataList.stream()
                .filter(data -> data.getUnitId() == unitId && data.getElderEpic() == elderEpic)
                .findFirst()
                .map(UnitEEPriorityData::getText)
                .orElse(null);
    }

    @Override
    public ImmutableList<Unit> createElderEpicPriorityList(ElderEpic elderEpic, ImmutableList<Unit> unitsToSearchFrom) {
        return dataList.stream()
                .filter(data -> data.getElderEpic() == elderEpic)
                .map(data -> CollectionsKt.firstOrNull(unitsToSearchFrom, unit -> unit.getId() == data.getUnitId()))
                .filter(Objects::nonNull)
                .collect(new ImmutableListCollector<>());
    }

}
