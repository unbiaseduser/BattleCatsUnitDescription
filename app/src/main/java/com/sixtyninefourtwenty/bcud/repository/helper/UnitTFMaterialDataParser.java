package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.UnitTFMaterialData;

import org.json.JSONArray;

import java.util.List;

import lombok.SneakyThrows;

@NonNullTypesByDefault
public final class UnitTFMaterialDataParser implements UnitTFMaterialDataSupplier {

    private final List<UnitTFMaterialData> dataList;

    @SneakyThrows
    public UnitTFMaterialDataParser(String json) {
        dataList = UnitTFMaterialData.SERIALIZER.listFromJson(new JSONArray(json));
    }

    @Override
    public ImmutableList<TFMaterialData> getMaterialListForUnitWithId(int id) {
        return dataList.stream()
                .filter(data -> data.getUnitId() == id)
                .findFirst()
                .map(UnitTFMaterialData::getMaterials)
                .map(ImmutableList::copyOf)
                .orElse(ImmutableList.of());
    }

}
