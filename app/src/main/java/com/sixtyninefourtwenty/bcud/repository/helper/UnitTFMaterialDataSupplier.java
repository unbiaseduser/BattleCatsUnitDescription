package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;

@NonNullTypesByDefault
public sealed interface UnitTFMaterialDataSupplier permits UnitTFMaterialDataParser, UnitTFMaterialDataParserCSV {
    ImmutableList<TFMaterialData> getMaterialListForUnitWithId(int id);
}
