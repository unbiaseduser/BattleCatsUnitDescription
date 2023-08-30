package com.sixtyninefourtwenty.bcud.repository.helper;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;

@NonNullTypesByDefault
public sealed interface UnitTalentDataSupplier permits UnitTalentDataParser, UnitTalentDataParserCSV {
    ImmutableList<TalentData> getTalentListForUnitWithId(int id);
    ImmutableList<Unit> createTalentPriorityList(Talent.Priority priority,
                                                 Talent.UnitType type,
                                                 ImmutableList<Unit> unitsToSearchFrom);
}
