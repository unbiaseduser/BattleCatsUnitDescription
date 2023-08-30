package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Hypermax;

@NonNullTypesByDefault
public sealed interface UnitHPDataSupplier permits UnitHPDataParser, UnitHPDataParserCSV {
    ImmutableList<Unit> createHypermaxPriorityList(Hypermax.Priority priority,
                                                   Hypermax.UnitType unitType,
                                                   ImmutableList<Unit> unitsToSearchFrom);
}
