package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import java.util.function.Function;
import java.util.function.IntFunction;

@NonNullTypesByDefault
public sealed interface UnitSupplier permits UnitParser, UnitParserCSV {
    ImmutableList<Unit> createMainList(UnitBaseData.Type type,
                                       IntFunction<ImmutableList<TFMaterialData>> tfMaterialsFunction,
                                       IntFunction<ImmutableList<TalentData>> talentDataFunction,
                                       Function<TalentData, Talent> talentDataToTalentFunction);
}
