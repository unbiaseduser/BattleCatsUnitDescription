package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;

import org.checkerframework.checker.nullness.qual.Nullable;

@NonNullTypesByDefault
public sealed interface UnitEEPriorityReasoningSupplier permits UnitEEPriorityReasoningDataParser, UnitEEPriorityReasoningDataParserCSV {
    @Nullable
    String getPriorityReasoningForUnitWithId(int unitId, ElderEpic elderEpic);
    ImmutableList<Unit> createElderEpicPriorityList(ElderEpic elderEpic, ImmutableList<Unit> unitsToSearchFrom);
}
